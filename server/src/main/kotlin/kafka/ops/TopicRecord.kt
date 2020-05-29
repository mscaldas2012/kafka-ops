package kafka.ops

/**
 *
 *
 * @Created - 5/29/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class  TopicRecord(val offset: Long,
                   val partition: Int,
                   val key: String,
                   val value: Any?)