package pl.umk.mat.zawodyweb.www;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class OpenIdConsumer {

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
    public boolean authRequest(String userSuppliedString, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            // configure the return_to URL where your application will receive
            // the authentication responses from the OpenID provider

            // --- Forward proxy setup (only if needed) ---
            // ProxyProperties proxyProps = new ProxyProperties();
            // proxyProps.setProxyName("proxy.example.com");
            // proxyProps.setProxyPort(8080);
            // HttpClientFactory.setProxyProperties(proxyProps);

            // perform discovery on the user-supplied identifier
            this.login = userSuppliedString;
            List discoveries = manager.discover(userSuppliedString);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // store the discovery information in the user's session
            req.getSession().setAttribute("openid-disc", discovered);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

            // Attribute Exchange example: fetching the 'email' attribute
            /*FetchRequest fetch = FetchRequest.createFetchRequest();
            fetch.addAttribute("email", "http://schema.openid.net/contact/email", true);
            fetch.addAttribute("fullname", "http://schema.openid.net/namePerson", true);
            // attach the extension to the authentication request
            authReq.addExtension(fetch);
             */

            SRegRequest sregReq = SRegRequest.createFetchRequest();
            sregReq.addAttribute("email", true);
            sregReq.addAttribute("fullname", true);
            // attach the extension to the authentication request
            authReq.addExtension(sregReq);


            //if (!discovered.isVersion2()) {
            // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
            // The only method supported in OpenID 1.x
            // redirect-URL usually limited ~2048 bytes
            res.sendRedirect(authReq.getDestinationUrl(true));
            return true;
            //} else {
            // Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

//                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("formredirection.jsp");
//                req.setAttribute("parameterMap", authReq.getParameterMap());
//                req.setAttribute("destinationUrl", authReq.getDestinationUrl(false));
//                dispatcher.forward(req, res);
//            }
        } catch (OpenIDException e) {
            // present error to the user
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
            // extract the parameters from the authentication response
            // (which comes in as a HTTP request from the OpenID provider)
            ParameterList response = new ParameterList(req.getParameterMap());

            // retrieve the previously stored discovery information
            DiscoveryInformation discovered = (DiscoveryInformation) req.getSession().getAttribute("openid-disc");

            // extract the receiving URL from the HTTP request
            StringBuffer receivingURL = req.getRequestURL();
            String queryString = req.getQueryString();
            if (queryString != null && queryString.length() > 0) {
                receivingURL.append("?").append(req.getQueryString());
            }

            // verify the response; ConsumerManager needs to be the same
            // (static) instance used to place the authentication request
            VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

            // examine the verification result and extract the verified identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

                if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
                    MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);

                    String _email = (String) ext.getParameters().getParameterValue("email");
                    String fullname = (String) ext.getParameters().getParameterValue("fullname");

                    if (_email != null && _email.isEmpty() == false && fullname != null) {
                        String[] names = fullname.split("[ ]+");
                        if (names.length >= 2) {
                            String _firstname = names[0];
                            String _lastname = names[1];
                            for (int i = 2; i < names.length; ++i) {
                                if (names[i].isEmpty() == false) {
                                    _lastname = names[i];
                                }
                            }

                            //this.login = verified.getIdentifier();
                            this.email = _email;
                            this.firstname = _firstname;
                            this.lastname = _lastname;

                            return verified;  // success
                        }
                    }
                }
            }
        } catch (OpenIDException e) {
            System.out.println("" + e.getLocalizedMessage());
            // present error to the user
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
