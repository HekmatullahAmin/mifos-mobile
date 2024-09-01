
plugins {
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    namespace = "org.mifos.mobile.core.qrcode"
}

dependencies {
    implementation(projects.core.model)

    api(libs.zxing.core)
    api(libs.squareup.retrofit.converter.gson)

    //cameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)
}