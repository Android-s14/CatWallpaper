package shared

import android.content.Context
import android.util.TypedValue

fun dpf(dip: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.resources.displayMetrics)

fun dp(dip: Float, context: Context) = dpf(dip, context).toInt()

fun Context.dpf(dip: Float) = dpf(dip, this)

fun Context.dp(dip: Float) = dpf(dip).toInt()