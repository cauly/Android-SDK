package kr.co.cauly.sdk.android.mediation.sample.natives

data class AdItem(
    var id: String? = null,
    var image: String? = null,
    var linkUrl: String? = null,
    var image_content_type: String? = null,
    var subtitle: String? = null,
    var icon: String? = null,
    var description: String? = null
) {
    // 기본 생성자 제공
    constructor() : this(null, null, null, null, null, null, null)
}