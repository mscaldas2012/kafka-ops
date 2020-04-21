package kafka.ops.security

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotEmpty

@ConfigurationProperties(value = "s2s-auth-config")
open class S2SAuthConfig {

    @NotEmpty
    lateinit var token: String
}