package com.hs.videoplayerdemo

import androidx.annotation.IntDef

@MustBeDocumented
@IntDef(AbnormalType.AbnormalTypeResultEmpty, AbnormalType.AbnormalTypeNetError, AbnormalType.AbnormalTypeResultError)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class AbnormalType {
    companion object {
        /** 返回值为空 */
        const val AbnormalTypeResultEmpty = 0
        /** 网络错误 */
        const val AbnormalTypeNetError = 1
        /** 接口返回值错误 */
        const val AbnormalTypeResultError = 2
    }
}
