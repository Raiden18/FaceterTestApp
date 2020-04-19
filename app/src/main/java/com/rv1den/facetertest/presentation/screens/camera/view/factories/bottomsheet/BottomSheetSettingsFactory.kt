package com.rv1den.facetertest.presentation.screens.camera.view.factories.bottomsheet

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rv1den.facetertest.domain.models.Resolution
import com.rv1den.facetertest.presentation.screens.camera.presenter.CameraPresenter
import com.rv1den.facetertest.presentation.screens.camera.view.settings.BottomSheetSettings

class BottomSheetSettingsFactory {

    fun create(
        presenter: CameraPresenter,
        resolution: Resolution
    ): BottomSheetDialogFragment {
        return BottomSheetSettings().apply {
            heightChangeListener = presenter::setImageHeight
            widthChangeListener = presenter::setImageWidth
            onApplyClickListener = presenter::applySettings
            this.resolution = resolution
        }
    }
}