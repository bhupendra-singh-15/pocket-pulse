package app.pocketpulse.service;

import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.entity.TransactionEntity;
import app.pocketpulse.entity.enums.CategoryType;
import app.pocketpulse.repository.ProfileRepository;
import app.pocketpulse.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNotificationService {

    private final ProfileRepository profileRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    //  Runs every day at 9 PM
    @Scheduled(cron = "0 0 21 * * ?")
//    @Scheduled(cron = "0 */2 * * * ?") // every 2 minutes
//    @Scheduled(cron = "0 * * * * ?")
    public void sendDailyNotifications() {

        List<ProfileEntity> users = profileRepository.findAll();

        LocalDate today = LocalDate.now();

        for (ProfileEntity user : users) {

            if (!Boolean.TRUE.equals(user.getIsActive())) continue;

            List<TransactionEntity> transactions =
                    transactionRepository.findByProfileAndDate(user, today);

            if (transactions.isEmpty()) {
                sendReminderEmail(user);
            } else {
                sendSummaryEmail(user, transactions);
            }
        }
    }

    //  Reminder
    private void sendReminderEmail(ProfileEntity user) {

        String subject = "Reminder: Add your daily transactions";

        String body = """
                <h2>Hello %s 👋</h2>
                <p>You haven't added any income or expense today.</p>
                <p>Keep your finances updated in <b>PocketPulse</b>.</p>
                <br>
                <p>Stay consistent 💪</p>
                """.formatted(user.getFullName());

        emailService.sendHtmlEmail(user.getEmail(), subject, body);
    }

    //  Summary
    private void sendSummaryEmail(ProfileEntity user, List<TransactionEntity> transactions) {

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        StringBuilder rows = new StringBuilder();

        for (TransactionEntity t : transactions) {

            boolean isIncome = t.getType() == CategoryType.INCOME;

            if (isIncome) {
                income = income.add(t.getAmount());
            } else {
                expense = expense.add(t.getAmount());
            }

            String bgColor = isIncome ? "#e6f4ea" : "#fdecea"; // green / red
            String typeColor = isIncome ? "#2e7d32" : "#c62828";

            rows.append("""
            <tr style="background-color:%s;">
                <td style="padding:10px; font-weight:bold; color:%s;">%s</td>
                <td style="padding:10px;">%s</td>
                <td style="padding:10px;">₹%s</td>
                <td style="padding:10px;">%s</td>
            </tr>
        """.formatted(
                    bgColor,
                    typeColor,
                    t.getType(),
                    t.getName(),
                    t.getAmount(),
                    t.getDate()
            ));
        }

        BigDecimal balance = income.subtract(expense);

        String body = """
        <html>
        <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f4f6f8;">

            <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;">

                <h2 style="text-align:center; color:#333;">📊 Daily Financial Summary</h2>

                <p style="font-size:16px;">Hello <b>%s</b> 👋</p>

                <!-- TRANSACTION TABLE -->
                <table style="width:100%%; border-collapse:collapse; margin-top:15px;">
                    <thead>
                        <tr style="background-color:#1976d2; color:white;">
                            <th style="padding:10px;">Type</th>
                            <th style="padding:10px;">Name</th>
                            <th style="padding:10px;">Amount</th>
                            <th style="padding:10px;">Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>

                <!-- SUMMARY SECTION -->
                <div style="margin-top:20px;">

                    <h3 style="color:#333;">Summary</h3>

                    <table style="width:100%%; border-collapse:collapse;">
                        <tr>
                            <td style="padding:10px; font-weight:bold;">💰 Total Income</td>
                            <td style="padding:10px; color:#2e7d32;">₹%s</td>
                        </tr>
                        <tr>
                            <td style="padding:10px; font-weight:bold;">💸 Total Expense</td>
                            <td style="padding:10px; color:#c62828;">₹%s</td>
                        </tr>
                        <tr style="background-color:#f1f8ff;">
                            <td style="padding:10px; font-weight:bold;">📊 Balance</td>
                            <td style="padding:10px; font-weight:bold;">₹%s</td>
                        </tr>
                    </table>
                </div>

                <hr style="margin:20px 0;">

                <p style="text-align:center; color:#777; font-size:12px;">
                    Keep tracking your finances with <b>PocketPulse</b> 🚀
                </p>

            </div>

        </body>
        </html>
        """.formatted(
                user.getFullName(),
                rows.toString(),
                income,
                expense,
                balance
        );

        emailService.sendHtmlEmail(user.getEmail(), "📊 Your Daily Summary", body);
    }
}