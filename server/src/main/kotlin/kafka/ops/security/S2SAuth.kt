package kafka.ops.security

import org.slf4j.LoggerFactory
import javax.inject.Singleton

/**
 * This class is a simple implementation to check for a secret from the caller.
 * The token can be configured on application. yml and the user needs to send it
 * as a header attribute.
 *
 * On your application.yaml, add the following:
 * s2s-auth-config:
 *     token: <<My Secret Token>>
 */

@Singleton
class S2SAuth(val config: S2SAuthConfig) {
    companion object {
        private val LOG = LoggerFactory.getLogger(S2SAuth::class.java)
    }

    @Throws( ServiceNotAuthorizedException::class)
        fun checkS2SCredentials(token: String?):Boolean {
            if (config.token != token) {
                LOG.error("Invalid token - Authentication Failed")
                throw ServiceNotAuthorizedException("Service not Authorized to call method!")
            }
            return true
        }
}