package model;

public interface MailService {
	void addMail(ZMail mail);
	void getMails(int id);
	int showMails();
}
