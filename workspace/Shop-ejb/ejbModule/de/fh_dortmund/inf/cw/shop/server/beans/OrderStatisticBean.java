package de.fh_dortmund.inf.cw.shop.server.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.mail.Session;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.OrderManagementLocal;
import de.fh_dortmund.inf.cw.shop.server.entities.Order;

/*
 * Diese Bean ist der Singleton-Variante vorzuziehen,
 * da sie in skalierbaren Anwendungen kein potentielles Bottleneck darstellt.
 */

@Stateless
public class OrderStatisticBean {

	private final String MAIL_ORDER_STATISTIC_TIMER = "MAIL_ORDER_STATISTIC_TIMER";
	
	@Resource(lookup = "java:app/mail/Shop")
	private Session mailSession;
	
	@Resource(name = "mailRecipient")
	private String mailRecipient;

	@Resource
	private TimerService timerService;
	
	@EJB
	private OrderManagementLocal orderManagement;
	
	// Wird ohne explizite Einstellungen jeden Tag um 0:00 getriggert.
	@Schedule(info = MAIL_ORDER_STATISTIC_TIMER, persistent = true)
	public void timeout(Timer timer) {
		if (timer.getInfo().equals(MAIL_ORDER_STATISTIC_TIMER)) {
			Calendar currentDateCalendar = new GregorianCalendar();
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentDateCalendar.set(Calendar.MINUTE, 0);
			currentDateCalendar.set(Calendar.SECOND, 0);
			currentDateCalendar.set(Calendar.MILLISECOND, 0);
			
			Calendar startingDateCalendar = new GregorianCalendar();
			startingDateCalendar.setTime(currentDateCalendar.getTime());
			startingDateCalendar.add(Calendar.DAY_OF_MONTH, -1);
			Date startingDate = startingDateCalendar.getTime();
			
			Calendar endDateCalendar = new GregorianCalendar();
			endDateCalendar.setTime(currentDateCalendar.getTime());
			endDateCalendar.add(Calendar.MILLISECOND, -1);
			Date endDate = endDateCalendar.getTime();
			
			// startingDate und endDate beschreiben den vorherigen Tag (0:00 bis 23:59:59.999)
			
			List<Order> orders = orderManagement.readAllOrders();
			int numberOfOrders = 0;
			double netAmount = 0.;
			double grossAmount = 0.;
			
			for(Order order : orders) {
				if (order.getOrderDate().compareTo(startingDate) <= 0 
						&& order.getOrderDate().compareTo(endDate) >= 0) {
					numberOfOrders++;
					netAmount += order.getInvoiceNetAmount();
					grossAmount += order.getInvoiceGrossAmount();
				}
			}

			String subject = "Bestellstatistik";
			String text = getHTMLText(startingDate, endDate, numberOfOrders, netAmount, grossAmount);
			System.out.println(String.format("## SEND MAIL\n#\n#\n# %s\n#\n%s", subject, text));
			
			/*
			try {
				MimeMessage mail = new MimeMessage(mailSession);
				mail.setFrom(new InternetAddress(mailSession.getProperty("mail.from"), "Shop"));
				mail.setRecipient(RecipientType.TO, new InternetAddress(mailRecipient));
				mail.setSubject(subject, "utf-8");
				mail.setContent(text, "text/plain; charset-utf-8");
				Transport.send(mail);
				
				System.out.println("Send Order-Statistic to " + mailRecipient);
			} catch (Exception e) {
				System.err.println("Error while sending the mail: " + e.getMessage());
			}
			*/
		}
	}
	
	private String getHTMLText(Date startingDate, Date endDate, int numberOfOrders, double netAmount, double grossAmount) {
		String text = "startingDate=" + startingDate;
		text += "\nendDate=" + endDate;
		text += "\nnumberOfOrders=" + numberOfOrders;
		text += "\nnetAmount=" + netAmount;
		text += "\ngrossAmount=" + grossAmount;
		
		return text;
	}
}
