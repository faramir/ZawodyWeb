package pl.umk.mat.zawodyweb.www;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class OpenIdConsumer {

    private static final Logger logger = Logger.getLogger(OpenIdConsumer.class);
    private ConsumerManager manager;
    private String returnToUrl;
    private String login;
    private String email;
    private String firstname;
    private String lastname;

    public OpenIdConsumer(String returnToUrl) throws ConsumerException {
        this.manager = new ConsumerManager();
        this.returnToUrl = returnToUrl;
    }

    /**
     * Send authorization request to OpenID server
     * @param userSuppliedString - OpenID username/login
     * @param req - current request object
     * @param res - current response object
     * @return - true if sent, false if error
     * @throws IOException - other problem
     */
    public boolean authorizationRequest(String userSuppliedString, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            List discoveries = manager.discover(userSuppliedString);

            DiscoveryInformation discovered = manager.associate(discoveries);

            req.getSession().setAttribute("openid-disc", discovered);

            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);
            SRegRequest sregReq = SRegRequest.createFetchRequest();

            sregReq.addAttribute("email", true);
            sregReq.addAttribute("fullname", true);
            authReq.addExtension(sregReq);

            res.sendRedirect(authReq.getDestinationUrl(true));

            return true;
        } catch (OpenIDException ex) {
            // logger.error("Exception in authRequest with '" + userSuppliedString + "'", ex);
        }

        return false;
    }

    /**
     * Processing response from OpenID server
     * @param req - request sent by OpenID server
     * @return Identifier or null if error
     */
    public Identifier verifyResponse(HttpServletRequest req) {
        try {
            ParameterList response = new ParameterList(req.getParameterMap());

            DiscoveryInformation discovered = (DiscoveryInformation) req.getSession().getAttribute("openid-disc");

            StringBuffer receivingURL = req.getRequestURL();
            String queryString = req.getQueryString();
            if (queryString != null && queryString.length() > 0) {
                receivingURL.append("?").append(req.getQueryString());
            }

            VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

                if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
                    MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);

                    String _email = (String) ext.getParameters().getParameterValue("email");
                    String _fullname = (String) ext.getParameters().getParameterValue("fullname");

                    if (_email != null && _email.isEmpty() == false && _fullname != null) {
                        String[] names = _fullname.split("[ ]+");
                        if (names.length >= 2) {
                            String _firstname = names[0];
                            String _lastname = names[1];
                            for (int i = 2; i < names.length; ++i) {
                                if (names[i].isEmpty() == false) {
                                    _lastname = names[i];
                                }
                            }

                            this.login = new URI(authSuccess.getIdentity()).getHost();
                            this.email = _email;
                            this.firstname = _firstname;
                            this.lastname = _lastname;

                            return verified;
                        }
                    }
                }
            }
        } catch (URISyntaxException ex) {
            logger.error("Exception in verifyResponse", ex);
        } catch (OpenIDException ex) {
            logger.error("Exception in verifyResponse", ex);
        }

        return null;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
}
