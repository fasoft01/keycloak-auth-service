package zw.co.fasoft;
/**
 * @author Fasoft
 * @date 2/May/2024
 */
public class Message {
    public static final String USER_ACCOUNT_CREATION_MESSAGE =
            "We are pleased to inform you that you have been appointed for the : {role} role for INSTALIPA. \n" +
                    "Your login credentials are provided below:\n\n"
                    +"Username: {username}" + " ,Password: {password}.\n\n" +
                    "Please use the above credentials to log in to the INSTALIPA admin  portal and commence your administrative duties.\n" +
                    "Should you have any questions or require assistance, feel free to contact our support team.\n " +
                    "{login-message}"+
                    "Best Regards, INSTALIPA Administration Team";

    public static final String SUCCESSFUL_UNSUBSCRIPTION_MESSAGE =
            "We're sorry to hear that you've decided to unsubscribe from INSTALIPA.\n" +
                    "Your feedback is important to us, and we'd like to understand how we can improve.\n" +
                    "If there's anything specific that led to this decision, please let us know.\n" +
                    "Thank you for being a part of INSTALIPA. We appreciate your time with us.\n" +
                    "If you ever decide to give us another chance, we'll be here to assist you.\n" +
                    "Best regards,\n" +
                    "INSTALIPA Administration Team";

    public static final String ACCOUNT_DEACTIVATION_MESSAGE = "We regret to inform you that your INSTALIPA account has been deactivated.\n" +
            "For any inquiries or assistance, please contact our support team.\n" +
            "We apologize for any inconvenience caused.";

    public static final String ACCOUNT_ACTIVATION_MESSAGE = "We are pleased to inform you that your INSTALIPA account has been successfully activated.\n" +
            "You can now enjoy all the features and benefits of our platform.\n" +
            "Should you have any questions or need assistance, feel free to contact our support team.\n" +
            "Thank you for choosing INSTALIPA!";

    public static final String PASSWORD_RESET_NOTIFICATION = "We are pleased to inform you that the password for your INSTALIPA account has been successfully reset.\n" +
            "You can now log in using your new password.\n" +
            "If you did not request a password reset, please contact our support team immediately.\n" +
            "Thank you for using INSTALIPA!";


    public static final String CLIENT_CREATION_MESSAGE =
            "We extend our warmest gratitude for choosing INSTALIPA and for creating your account. \n  "+
                    "The INSTALIPA account has been created successfully. \n" +
                    " Please log in using your credentials on the mobile app to start using INSTALIPA. \n" +
                    "Thank you once again for entrusting INSTALIPA with your financial needs."+
                    "Regards,\n" + "INSTALIPA Adminstration Team";
    public static String ACCOUNT_CREATION_MESSAGE =
            "We are pleased to inform you that your INSTALIPA account has been successfully created as a MERCHANT.\n\n" +
                    "Your login credentials are as follows:\n" +
                    "Username: {username}\n" + "Password: {password}\n\n"+
                    "Click here to login {messageLink}.\n\n"
                    + "Regards,\n" + "INSTALIPA Team";

    public static String ADMIN_LOGIN_LINK_MESSAGE =
            "Click here to login {epay-admin-login-link}.\n\n";

    public static String MERCHANT_LOGIN_LINK =
            "Click here to login {epay-merchant-login-link}";

    public static String merchantOnboardAction = "Merchant Onboard";
    public static String clientOnboardAction = "Client Onboard";

    public static String getAccountCreationMessage() {
        return ACCOUNT_CREATION_MESSAGE;
    }

    public static void setAccountCreationMessage(String accountCreationMessage) {
        ACCOUNT_CREATION_MESSAGE = accountCreationMessage;
    }
}