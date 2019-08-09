package com.yenen.ahmet.customstationview

data class StationModel(
    var isStartSecondLineAnimator: Boolean,
    var isStartFirstLineAnimator: Boolean,
    var isStartAnimationCircle: Boolean,
    var centerCircleColor: Int,
    var stationLineDrawType: Short,
    var lineAnimatorValue: Float,
    var firstLineAnimatorValue:Float,
    var secondLineAnimatorValue:Float
)