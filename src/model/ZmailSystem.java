package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZmailSystem {
	List<User> users;

	public ZmailSystem() {
		users = new ArrayList<>();
	}

	private static final Logger logger = LogManager.getLogger(ZmailSystem.class);

	public static void main(String[] args) {
		Scanner getInput = new Scanner(System.in);

		logger.info("Email system started");
		Authentication outh = new Authentication();
		ZmailSystem zmailUsers = new ZmailSystem();
		System.out.println(Color.GREEN
				+ "__________________________________________________\n|                                                 |\n|                                                 |");
		System.out.println(
				"|------------  Welcome to ZMail system -----------|\n|                                                 |");
		System.out.println("|_________________________________________________|" + Color.RESET);

//		System.out.println("\nIf you want enter the application you must signin/signup");
		int times = 0;
		mainloop: while (true) {
			byte userLoginOption = 0;
			while (true) {
				try {
					if (times == 0) {

						System.out.println(Color.CYAN + "-----------------");
						System.out.println("|               |");
						System.out
								.println("|1. Sign in     |\n|               |\n|2. Sign up     |\n|               |");
						System.out.println("-----------------" + Color.RESET);
						System.out.println(Color.YELLOW + "Enter your Option :" + Color.RESET);
						times++;
					} else {

						System.out.println(Color.CYAN + "-----------------");
						System.out.println("|    Login      |");
						System.out.println("-----------------\n|               |");
						System.out.println(
								"|1. Sign in     |\n|               |\n|2. Sign up     |\n|               |\n|3. Exit        |\n|               |");
						System.out.println("-----------------" + Color.RESET);
						System.out.println(Color.YELLOW + "Enter your Option :" + Color.RESET);
						times++;
					}

					userLoginOption = getInput.nextByte();
					if (userLoginOption <= 0 || (userLoginOption > 2 && times == 1)) {
						System.out.println(Color.RED + "Enter the valid option : " + Color.RESET);
						continue;
					}

					break;
				} catch (Exception e) {
					System.out.println("Enter the valid value");
					getInput.nextLine();
				}
			}
			if (times > 1 && userLoginOption == 3) {
				break mainloop;
			}
			getInput.nextLine();
			User user = null;
			switch (userLoginOption) {
			case 1:
				logger.info("user enter into signin");
				outerloop: while (true) {
					String userMail = "";

					while (true) {
						System.out.println("Enter your zmail: ");
						userMail = getInput.nextLine().trim();
						if (userMail.isEmpty()) {
							System.out.println("Email cannot be empty. Try again.");
							continue;
						}
						break;
					}

					System.out.println("Enter your password: ");
					String password = getInput.nextLine();

					try {
						user = outh.login(userMail, password);
						if (user == null) {
							System.out.println(" Invalid username or password. Please try again.");
							continue outerloop;
						}
						break outerloop;
					} catch (Exception e) {
						System.out.println(" Invalid username or password. Please try again.");
						continue outerloop;
					}
				}

				break;
			case 2:
				logger.info("user enter into singup");
				System.out.println(Color.YELLOW + "Enter you name " + Color.RESET);
				String name = getInput.nextLine();
				Date dateOfBirth = null;
				while (dateOfBirth == null) {
					try {
						System.out.println(Color.YELLOW + "Enter your date of birth (yyyy-MM-dd): ");
						String dobInput = getInput.nextLine();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						dateOfBirth = sdf.parse(dobInput);
						System.out.println("Your date of birth is: " + dateOfBirth);
					} catch (ParseException e) {
						System.out.println("Invalid date format. Please use YYYY-MM-dd.");
					}
				}
				String type = "";
				while (true) {

					System.out.println("Enter the Zmail type (public, private)");
					type = getInput.nextLine();
					if ((type.trim().equalsIgnoreCase("public")) || (type.trim().equalsIgnoreCase("private"))) {
						break;
					}
					continue;
				}
				System.out.println("Enter the password for your zmail" + Color.RESET);
				String newPassword = getInput.nextLine();

				user = outh.signUp(name, dateOfBirth, type, newPassword);
				break;
			default:
				System.out.println("Enter the valid option");
			}

			if (user != null) {
				logger.info("user logined userid : " + user.getUserId());
				byte userOption = 0;
				while (userOption != 8) {
					while (true) {
						try {

							System.out.println(Color.CYAN + "-------------------------");
							System.out.println(
									"|1. Inbox               |\n|2. Groups              |\n|3. Filters             |\n|4. Saved               |\n|5. Sent                |\n|6. New                 |\n|7. Favorites           |\n|8. Sign out            |\n|9. Exit                |");

							System.out.println(Color.CYAN + "-------------------------");
							System.out.println(Color.YELLOW + "Enter your option : " + Color.RESET);
							userOption = getInput.nextByte();
							if (userOption <= 0 && userOption > 4) {
								System.out.println("Enter the valid option --");
								continue;
							}
							break;
						} catch (Exception e) {
							System.out.println("Enter the valid value --");
							getInput.nextLine();
						}
					}

					switch (userOption) {
					case 1:
						logger.info("user entered into inbox userid : " + user.getUserId());
						byte viewOption = 0;
						while (viewOption != 5) {
							while (true) {
								try {

									System.out.println(Color.CYAN + "--------------------");
									System.out.println(
											"|1. View Mail      |\n|2. Read Mail      |\n|3. Unread Mails   |\n|4. Search Mails   |\n|5. Back           |"
													+ Color.RESET);
									System.out.println(Color.CYAN + "--------------------");

									System.out.println(Color.YELLOW + "Enter your Option :" + Color.RESET);
									viewOption = getInput.nextByte();
									break;
								} catch (Exception e) {
									System.out.println("Enter the valid input");
									getInput.nextLine();
								}

							}

							switch (viewOption) {
							case 1:
								logger.info("get into view mails in inbox : " + user.getUserId());
								int count = user.inbox.showMails(user.getMailId());
								if (count > 0) {
									int mailId = 0;
									while (true) {
										try {
//										
											System.out.println("Enter the zmail id");
											mailId = getInput.nextInt();
											break;
										} catch (Exception e) {
											System.out.println("Enter the valid input");
											getInput.nextLine();
										}
									}
									ZMail mail = user.inbox.getMail(mailId, user.getMailId());
									if (mail != null) {
										byte userViewOption = 0;
										outerLoop: while (userViewOption != 5) {
											while (true) {
												try {
													System.out.println(Color.CYAN + "-------------------------");
													System.out.println(
															"|1. view Mail           |\n|2. Add to Save         |\n|3. Add to favorite     |\n|4. Delete Mail         |\n|5. Back                |");
													System.out.println(Color.CYAN + "-------------------------");

													System.out.println(
															Color.YELLOW + "Enter your Option :" + Color.RESET);
													userViewOption = getInput.nextByte();
													break;
												} catch (Exception e) {
													System.out.println(
															Color.RED + "Enter the valid input " + Color.RESET);
												}
											}
											switch (userViewOption) {
											case 1:
												logger.info("showfullmail userid :" + user.getUserId());
												mail.showFullDetails();
												mail.markAsRead();
												break;
											case 2:
												logger.info("add to save list userid : " + user.getUserId());
												user.save.addMail(mail);
												break;
											case 3:
												user.favorite.addMail(mail);
												logger.info("add to favorite list user : " + user.getUserId());
											case 4:
												logger.info("remove mail userid : " + user.getUserId());
												user.inbox.removeMail(user.getUserId(), mailId, user.getMailId());
												break outerLoop;
											case 5:
												break outerLoop;
											default:
												System.out.println(Color.RED + "Enter the valid input " + Color.RESET);
											}
										}
									} else {
										System.out.println(Color.RED + "Mail id is invalid  " + Color.RESET);
									}

								} else {
									System.out.println(Color.YELLOW + "you don't have mails" + Color.RESET);
								}
								// --------
								break;
							case 2:
								logger.info("already viewed mails userid : " + user.getUserId());
								int count1 = user.inbox.alreadyRead(user.getMailId());
								if (count1 > 0) {
									int mailId = 0;
									while (true) {
										try {
//										
											System.out.println("Enter the zmail id");
											mailId = getInput.nextInt();
											break;
										} catch (Exception e) {
											System.out.println("Enter the valid input");
											getInput.nextLine();
										}
									}
									ZMail mail = user.inbox.getMail(mailId, user.getMailId());
									if (mail != null) {
										byte userViewOption = 0;
										outerLoop: while (userViewOption != 5) {
											while (true) {
												try {
													System.out.println(Color.CYAN + "-------------------------");
													System.out.println(
															"|1. view Mail           |\n|2. Add to Save         |\n|3. Add to favorite     |\n|4. Delete Mail         |\n|5. Back                |");
													System.out.println(Color.CYAN + "-------------------------");

													System.out.println(
															Color.YELLOW + "Enter your Option :" + Color.RESET);
													userViewOption = getInput.nextByte();
													break;
												} catch (Exception e) {
													System.out.println(
															Color.RED + "Enter the valid input " + Color.RESET);
												}
											}
											switch (userViewOption) {
											case 1:
												logger.info("view full mail : " + user.getUserId());
												mail.showFullDetails();
												mail.markAsRead();
												break;
											case 2:
												user.save.addMail(mail);
												logger.info("added into save : " + user.getUserId());
												break;
											case 3:
												user.favorite.addMail(mail);
												logger.info("added into favorite : " + user.getUserId());
											case 4:
												user.inbox.removeMail(user.getUserId(), mailId, user.getMailId());
												logger.info("mail deleted : " + user.getUserId());
												break outerLoop;
											default:
												System.out.println(Color.RED + "Enter the valid input " + Color.RESET);
											}
										}
									} else {
										System.out.println(Color.RED + "Mail id is invalid  " + Color.RESET);
									}

								} else {
									System.out.println(Color.YELLOW + "you don't have mails" + Color.RESET);
								}
								// -
								break;
							case 3:
								logger.info("yet to viewed mails  : " + user.getUserId());
								int count2 = user.inbox.yetToRead(user.getMailId());
								if (count2 > 0) {
									int mailId = 0;
									while (true) {
										try {
//										
											System.out.println("Enter the zmail id");
											mailId = getInput.nextInt();
											break;
										} catch (Exception e) {
											System.out.println("Enter the valid input");
											getInput.nextLine();
										}
									}
									ZMail mail = user.inbox.getMail(mailId, user.getMailId());
									if (mail != null) {
										byte userViewOption = 0;
										outerLoop: while (userViewOption != 5) {
											while (true) {
												try {
													System.out.println(Color.CYAN + "-------------------------");
													System.out.println(
															"|1. view Mail           |\n|2. Add to Save         |\n|3. Add to favorite     |\n|4. Delete Mail         |\n|5. Back                |");
													System.out.println(Color.CYAN + "-------------------------");

													System.out.println(
															Color.YELLOW + "Enter your Option :" + Color.RESET);
													userViewOption = getInput.nextByte();
													break;
												} catch (Exception e) {
													System.out.println(
															Color.RED + "Enter the valid input " + Color.RESET);
												}
											}
											switch (userViewOption) {
											case 1:
												mail.showFullDetails();
												mail.markAsRead();
												logger.info("view full mail : " + user.getUserId());
												break;
											case 2:
												user.save.addMail(mail);
												logger.info("added into save : " + user.getUserId());
												break;
											case 3:
												user.favorite.addMail(mail);
												logger.info("added into favorite : " + user.getUserId());
											case 4:
												user.inbox.removeMail(user.getUserId(), mailId, user.getMailId());
												logger.info("Remove mail : " + user.getUserId());
												break outerLoop;
											default:
												System.out.println(Color.RED + "Enter the valid input " + Color.RESET);
											}
										}
									} else {
										System.out.println(Color.RED + "Mail id is invalid  " + Color.RESET);
									}

								} else {
									System.out.println(Color.YELLOW + "you don't have mails" + Color.RESET);
								}
								// -
								break;
							case 4:
								getInput.nextLine();

								logger.info("search mails  : " + user.getUserId());
								int mailCount = user.inbox.showMails(user.getMailId());
								if (mailCount > 0) {
									System.out.println(Color.YELLOW + "Enter the your search option : " + Color.RESET);
									String search = getInput.nextLine();
									user.inbox.searchEmail(user.getMailId(), search);
								} else {
									System.out.println(Color.YELLOW + "you don't have mails" + Color.RESET);
								}
								break;
							case 5:
								break;
							default:
								System.out.println(Color.RED + "Enter the valid input : " + Color.RESET);

							}
						}
						break;
//		======================================== group oparations ====================================
					case 2:
						logger.info("enter into group : "+user.getUserId());
						byte groupOption = 0;
						while (groupOption != 3) {
							while (true) {
								try {
									System.out.println(Color.CYAN + "--------------------");
									System.out.println(
											"|1. view group     |\n|2. add group      |\n|3. Back           |");
									System.out.println(Color.CYAN + "--------------------" + Color.RESET);
									System.out.println(Color.YELLOW + "Enter your Option :" + Color.RESET);
									groupOption = getInput.nextByte();
									break;
								} catch (Exception e) {
									System.out.println("Enter the valid input  ");
									getInput.nextLine();
								}
							}

							switch (groupOption) {
							case 1:
								logger.info("view group : "+user.getUserId());
								int groupCount = user.showGroups();
								if (groupCount > 0) {
									Group groupObj = null;
									int groupId = 0;
									while (true) {
										try {

											System.out.println("Enter the group id");
											groupId = getInput.nextInt();
											groupObj = user.getGroup(groupId);

											if (groupObj == null) {
												System.out
														.println(Color.RED + "Enter the valid group id" + Color.RESET);
												continue;
											}
											break;
										} catch (Exception e) {
											System.out.println(Color.RED + "Enter the valid input " + Color.RESET);
											getInput.nextLine();
										}
									}

									byte groupViewOption = 0;

									// ========= leader option ============

									if (groupObj.getLeaderId() == user.getUserId()) {
										logger.info(user.getUserId()+" is group admin");
										while (groupViewOption != 5) {

											while (true) {
												try {
													System.out.println(Color.CYAN + "--------------------");

													System.out.println(
															"|1. view users     |\n|2. Add users      |\n|3. View Mails     |\n|4. share Mails    |\n|5. Back           |");
													System.out.println("--------------------" + Color.RESET);
													System.out.println(
															Color.YELLOW + "Enter your option : " + Color.RESET);
													groupViewOption = getInput.nextByte();
													break;
												} catch (Exception e) {
													// TODO: handle exception
													System.out.println("Enter the valid input(1-4) ");
													getInput.nextLine();
												}
											}

											switch (groupViewOption) {
											case 1:
												logger.info("show users" +user.getUserId());
												groupObj.showUsers();
												break;
											case 2:
												logger.info("add user into group" +user.getUserId());
												String usermail = "";
												getInput.nextLine();
												try {

													System.out.println("Enter the user mail");
													usermail = getInput.nextLine();
													if (usermail.length() < 1) {
														System.out.println("Enter the valid usermail");
														getInput.nextLine();
														continue;
													}
													User userObj = zmailUsers.getUser(usermail);
													if (userObj == null) {
														System.out.println("Enter the valid usermail");
														getInput.nextLine();
														continue;
													}
													groupObj.addUser(groupId, userObj);
													userObj.addGroup(groupObj);
													break;
												} catch (Exception e) {
													System.out.println("Enter the valid usermail");
													getInput.nextLine();
												}
												break;
											case 3:
												logger.info("list all the mails in group : "+user.getUserId());
												groupObj.showMails();
												break;
											case 4:
												getInput.nextLine();
												System.out.println("subject ");
												String subject = getInput.nextLine();
												System.out.println("Enter the cc");
												String cc = getInput.nextLine();
												System.out.println("Enter the content : ");
												String content = getInput.nextLine();
												ZMail mail = new ZMail(user.getMailId(), user.getMailId(), cc,
														new Date(), subject, content);
												mail.send();
												try {
													User reciver = zmailUsers.getUser(cc);
													Filter filter = reciver.getFilter(user.getMailId());
													if (filter != null) {
														filter.addMail(mail);
													} else {
														reciver.inbox.addMail(mail);
													}
												} catch (Exception e) {
													System.out.println("invalid user id");
												}
												groupObj.addMail(groupObj.getGroupId(), mail);
												logger.info("send mail into group : "+user.getUserId());
												break;
											case 5:
												logger.info("step back into groups : "+user.getUserId());
												break;
											default:
												System.out.println("Enter the valid input");
											}

										}
									}

									// ========= user option ========

									else {
										while (groupViewOption != 4) {

											while (true) {
												try {

													System.out.println(Color.CYAN + "--------------------");

													System.out.println(
															"|1. view users     |\n|2. View Mails     |\n|3. share Mails    |\n|4. Back           |");
													System.out.println("--------------------" + Color.RESET);
													System.out.println(
															Color.YELLOW + "Enter your option : " + Color.RESET);
													groupViewOption = getInput.nextByte();
													break;
												} catch (Exception e) {
													// TODO: handle exception
													System.out.println("Enter the valid input(1-4) ");
													getInput.nextLine();
												}
											}

											switch (groupViewOption) {
											case 1:
												groupObj.showUsers();
												logger.info("show users" +user.getUserId());
												
												break;
											case 2:
												groupObj.showMails();
												logger.info("list all the mails in group : "+user.getUserId());
												break;
											case 3:
												getInput.nextLine();
												System.out.println("subject ");
												String subject = getInput.nextLine();
												System.out.println("Enter the cc");
												String cc = getInput.nextLine();
												System.out.println("Enter the content : ");
												String content = getInput.nextLine();
												ZMail mail = new ZMail(user.getMailId(), user.getMailId(), cc,
														new Date(), subject, content);
												mail.send();
												try {
													User Cc = zmailUsers.getUser(cc);
													Filter filter = Cc.getFilter(user.getMailId());
													if (filter != null) {
														filter.addMail(mail);
													} else
														Cc.inbox.addMail(mail);
												} catch (Exception e) {
													System.out.println("invalid user id");
												}
												groupObj.addMail(groupObj.getGroupId(), mail);
												logger.info("send mail into group : "+user.getUserId());
												break;
											case 4:
												logger.info("step back into groups : "+user.getUserId());
												break;
											default:
												System.out.println("Enter the valid input");
											}

										}
									}
								} else {
									System.out.println("You don't have a group");
								}
								break;
							case 2:
								logger.info("creating group : "+user.getUserId());
								getInput.nextLine();
								System.out.println("Enter the group name");
								String groupName = getInput.nextLine();
								Group obj = user.createAndAssignGroup(groupName);
								obj.addUser(obj.getGroupId(), user);
								break;
							case 3:
								break;
							default:
								System.out.println("Enter the valid data");
							}
						}

						break;
					case 3:
						logger.info("enter into  filter : "+user.getUserId());
						user.showFilter();
						byte filterOption = 0;
						while (filterOption != 3) {
							while (true) {
								try {
									System.out.println(Color.CYAN + "--------------------");
									System.out.println(
											"|1. view filter    |\n|2. add filter     |\n|3. Back           |");
									System.out.println(Color.CYAN + "--------------------" + Color.RESET);
									System.out.println(Color.YELLOW + "Enter your Option :" + Color.RESET);
									filterOption = getInput.nextByte();
									break;
								} catch (Exception e) {
									System.out.println("Enter the valid input ");

									getInput.nextLine();
								}
							}

							switch (filterOption) {
							case 1:
								logger.info("show the filter : "+user.getUserId());
								int filterLength = user.showFilter();
								if (filterLength > 0) {
									int filterId = 0;
									while (true) {
										try {
											System.out.println("Enter the filter Id ");
											filterId = getInput.nextInt();
											break;
										} catch (Exception e) {
											// TODO: handle exception
											System.out.println("enter the valid filter id");
										}
									}

									Filter obj = user.getFilter(filterId);
									if (obj != null) {
										int filterUserOption = 0;

										while (filterUserOption != 4) {
											while (true) {
												try {

													System.out.println(
															"1) view mails\n2) Add user\n3) View users\n4) Back ");
													filterUserOption = getInput.nextInt();
													break;
												} catch (Exception e) {
													System.out.println("Enter the valid data :");
													getInput.nextLine();
												}
											}

											switch (filterUserOption) {
											case 1:
												logger.info("view filter mails : "+user.getUserId());
												obj.showMails();
												break;
											case 2:
												logger.info("add user into filter : "+user.getUserId());
												String userMail = null;
												getInput.nextLine();
												while (true) {
													try {
														System.out.println("Enter the user mail");
														userMail = getInput.nextLine();
														User userObj = zmailUsers.getUser(userMail);
														obj.addUser(filterId, userObj);
														break;
													} catch (Exception e) {
														System.out.println("Enter the valid mail");
														getInput.nextLine();
													}
												}

												break;
											case 3:
												logger.info("show all filtered users : "+user.getUserId());
												obj.showUsers();
												break;
											case 4:
												break;
											default:
												System.out.println("Enter the valid input");
											}

										}
									} else {
										System.out.println("Enter the valid filter id");
									}
								} else {
									System.out.println("you don't have a filters");
								}
								break;
							case 2:
								logger.info("adding new filter : "+user.getUserId());
								try {
									getInput.nextLine();
									System.out.println("Enter the filter name ");
									String filterName = getInput.nextLine();
									if (filterName.length() < 1) {
										System.out.println("Enter the valid input ");
									}
									user.createAndAssignFilter(filterName);
								} catch (Exception e) {
									System.out.println("Enter the valid input ");
								}
								break;
							case 3:
								break;
							default:
								System.out.println("Enter the valid input ");
							}
						}
						break;
					case 4:
						logger.info("entered into save collection : "+user.getUserId());
						int count = user.save.showMails();
						if (count > 0) {
							byte saveViewOption = 0;
							while (true) {
								try {

									System.out.println("1)viewMail\n2)Remove save\n3)Back");
									saveViewOption = getInput.nextByte();
									break;
								} catch (Exception e) {
									System.out.println("Enter the valid input");
									getInput.nextLine();
								}

							}

							switch (saveViewOption) {
							case 1:
								logger.info("view mail in save : "+user.getUserId());
								int mailId = 0;
								while (true) {
									try {
										System.out.println("Enter the zmail id");
										mailId = getInput.nextInt();
										break;
									} catch (Exception e) {
										System.out.println("Enter the valid input");
										getInput.nextLine();
									}
								}
								user.save.getMails(mailId);

								// --------
								break;
							case 2:
								logger.info("remove the saved mails : "+user.getUserId());
								int saveMailId = 0;
								
								while (true) {
									try {
										System.out.println("Enter the zmail id");
										saveMailId = getInput.nextInt();
										break;
									} catch (Exception e) {
										System.out.println("Enter the valid input");
										getInput.nextLine();
									}
								}
								user.save.removeSavedMail(user.getUserId(), saveMailId);
								break;
							default:
								System.out.println("Enter the valid input : ");
							}
						} else {
							System.out.println("You don't have a mail");
						}
						break;
					case 5:

						logger.info("entered into sharedMails collection : "+user.getUserId());
						int count1 = user.sharedMails.showMails();
						if (count1 > 0) {
							byte sharedMailsOption = 0;
							while (true) {
								try {

									System.out.println("1)viewMail\n2)Back");
									sharedMailsOption = getInput.nextByte();
									break;
								} catch (Exception e) {
									System.out.println("Enter the valid input");
									getInput.nextLine();
								}

							}

							switch (sharedMailsOption) {
							case 1:
								logger.info("enter into view mail : "+user.getMailId());
								int mailId = 0;
								while (true) {
									try {
										System.out.println("Enter the zmail id");
										mailId = getInput.nextInt();
										break;
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println("Enter the valid input");
										getInput.nextLine();
									}
								}
								user.sharedMails.getMails(mailId);
								// --------
								break;
							case 2:
								break;
							default:
								System.out.println("Enter the valid input : ");
							}
						} else {
							System.out.println("You don't have a mail");
						}
						break;
					case 6:
						getInput.nextLine();
						logger.info("create new mail : "+ user.getUserId());
						System.out.println("To -> mention the zmail id who you want to sent mail");
						String to = getInput.nextLine().trim();
						System.out.println("subject ");
						String subject = getInput.nextLine();
						System.out.println("Enter the cc");
						String cc = getInput.nextLine();
						System.out.println("Enter the content : ");
						String content = getInput.nextLine();
						ZMail mail = new ZMail(user.getMailId(), to, cc, new Date(), subject, content);
						mail.send();
						try {
							User reciver = zmailUsers.getUser(to.trim());
							Filter filter = reciver.getFilter(user.getMailId());
							if (filter != null) {
								filter.addMail(mail);
							} else
								reciver.inbox.addMail(mail);
						} catch (Exception e) {
							System.out.println("invalid user id");
						}
						try {
							User CC = zmailUsers.getUser(cc);
							CC.inbox.addMail(mail);

						} catch (Exception e) {
							System.out.println("Invalid cc");
						}
						user.sharedMails.addMail(mail);
						break;
					case 7:

						logger.info("entered into favorite collection : "+user.getMailId());
						
						int count3 = user.favorite.showMails();
						if (count3 > 0) {
							byte favoriteViewOption = 0;
							outerloop: while (favoriteViewOption != 3) {
								while (true) {
									try {

										System.out.println("1)viewMail\n2)Remove favorite\n3)Back");
										favoriteViewOption = getInput.nextByte();
										break;
									} catch (Exception e) {
										System.out.println("Enter the valid input");
										getInput.nextLine();
									}

								}

								switch (favoriteViewOption) {
								case 1:
									logger.info("view mail in favorite");
									int mailId = 0;
									while (true) {
										try {
											System.out.println("Enter the zmail id");
											mailId = getInput.nextInt();
											break;
										} catch (Exception e) {
											System.out.println("Enter the valid input");
											getInput.nextLine();
										}
									}
									user.favorite.getMails(mailId);

									// --------
									break;
								case 2:

									logger.info("remove mails in remove : "+user.getMailId());
									int favoriteMailId = 0;
									while (true) {
										try {
											System.out.println("Enter the zmail id");
											favoriteMailId = getInput.nextInt();
											break;
										} catch (Exception e) {
											System.out.println("Enter the valid input");
											getInput.nextLine();
										}
									}
									user.favorite.removeFavoriteMail(user.getUserId(), favoriteMailId);
									break outerloop;
								case 3:
									break;
								default:
									System.out.println("Enter the valid input : ");
								}
							}
						} else {
							System.out.println("You don't have a mail");
				        	logger.warn("You don't have a mail");
						}
						break;
					case 8:
						logger.info("user logout : " + user.getMailId());
						break;
					case 9:
						System.out.println("============= Thank You!!! ============");
						break mainloop;
					default:
						System.out.println("Enter the valid option : ");
					}
				}
			} else {
				System.out.println("No user found");
			}
		}
		getInput.close();
	}

	void loadUserFromDb() {

		String sql = "SELECT * FROM users";

		try (PreparedStatement stm = Connect.getConnection().prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

			while (rs.next()) {
				int userId = rs.getInt("userId");
				String name = rs.getString("name");
				java.sql.Date dob = rs.getDate("dateOfBirth");
				String mailId = rs.getString("mailId");
				String type = rs.getString("type");

				User user = new User(userId, name, dob, mailId, type);
				users.add(user);
			}

		} catch (Exception e) {
			System.out.println("Error loading users: " + e.getMessage());
			e.printStackTrace();
        	logger.error(e.getMessage());
		}
	}

	void addUser(User u) {
		users.add(u);
	}

	User getUser(String mail) {
		if (users.isEmpty())
			loadUserFromDb();
		for (User u : users) {
			if (u.getMailId().equals(mail))
				return u;
		}
		return null;
	}
	// show data ------

	void showUserData() {
		if (users.isEmpty())
			loadUserFromDb();
		for (User u : users) {
			System.out.println(u.getUserDetails());
		}
	}
}
