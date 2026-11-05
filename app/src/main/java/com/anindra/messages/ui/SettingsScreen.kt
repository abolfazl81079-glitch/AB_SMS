package com.anindra.messages.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.anindra.messages.AppViewModel
import com.anindra.messages.R
import com.anindra.messages.data.CustomAvatarStore
import com.anindra.messages.data.PeriodicBackupScheduler
import com.anindra.messages.data.SettingsStore
import com.anindra.messages.data.SimCard
import com.anindra.messages.data.SimCards
import com.anindra.messages.data.SimLabel
import com.anindra.messages.data.SimLabels
import com.anindra.messages.data.SimSelection
import com.anindra.messages.data.SwipeAction
import com.anindra.messages.sms.NotificationHelper
import com.anindra.messages.ui.theme.LocalLargeTouchTargets

private enum class PinDialogMode { SET, ENTER }

/** Which backup format the picked file belongs to. */
private enum class ImportSource { OWN_BACKUP, SMS_IE }

/** Searchable item model for settings */
private data class SearchSettingItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val category: String,
    val keywords: List<String>,
    val isSwitch: Boolean = false,
    val checked: Boolean = false,
    val onChecked: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    vm: AppViewModel,
    onBack: () -> Unit,
    onOpenTrash: () -> Unit = {},
    onOpenAdvanced: () -> Unit = {},
    onOpenSpamBlocked: () -> Unit = {},
    onOpenInbox: () -> Unit = {},
    onOpenScheduled: () -> Unit = {},
    scrollState: ScrollState = rememberScrollState()
) {
    var searching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    BackHandler(enabled = searching) {
        searching = false
        searchQuery = ""
    }
    BackHandler(enabled = !searching, onBack = onBack)

    val context = LocalContext.current
    val revision by vm.settings.revision.collectAsState()
    val scheduledAll by vm.scheduledMessages().collectAsState(initial = emptyList())
    val scheduledCount = if (scheduledAll.isEmpty()) null
    else context.getString(R.string.settings_scheduled_count, scheduledAll.size)

    // Dialog state
    var themeDialog by remember { mutableStateOf(false) }
    var languageDialog by remember { mutableStateOf(false) }
    var calendarDialog by remember { mutableStateOf(false) }
    var previewLinesDialog by remember { mutableStateOf(false) }
    var cellColorDialog by remember { mutableStateOf(false) }
    var cellStyleDialog by remember { mutableStateOf(false) }
    var cellOpacityDialog by remember { mutableStateOf(false) }
    var dividerColorDialog by remember { mutableStateOf(false) }
    var launcherIconDialog by remember { mutableStateOf(false) }
    var borderOpacitySliderDialog by remember { mutableStateOf(false) }
    var borderWidthSliderDialog by remember { mutableStateOf(false) }
    var messageCardShapeDialog by remember { mutableStateOf(false) }
    var delaySecondsDialog by remember { mutableStateOf(false) }
    var swipeLeftDialog by remember { mutableStateOf(false) }
    var swipeRightDialog by remember { mutableStateOf(false) }
    var soundDialog by remember { mutableStateOf(false) }
    var simDialog by remember { mutableStateOf(false) }
    var importModeDialog by remember { mutableStateOf(false) }
    var importSourceDialog by remember { mutableStateOf(false) }
    var vipTokenDialog by remember { mutableStateOf(false) }
    var vipThemesDialog by remember { mutableStateOf(false) }
    var tokenInput by remember { mutableStateOf("") }
    var tokenError by remember { mutableStateOf(false) }

    // Reactive setting values
    var hideNotifBody by remember(revision) { mutableStateOf(vm.settings.hideNotificationBody) }
    var notificationSound by remember(revision) { mutableStateOf(vm.settings.notificationSound) }
    var privacyMode by remember(revision) { mutableStateOf(vm.settings.privacyModeEnabled) }
    var appLock by remember(revision) { mutableStateOf(vm.settings.appLockEnabled) }
    var delayedSending by remember(revision) { mutableStateOf(vm.settings.delayedSendingEnabled) }
    var delaySeconds by remember(revision) { mutableIntStateOf(vm.settings.delaySeconds) }
    var deliveryReports by remember(revision) { mutableStateOf(vm.settings.deliveryReportsEnabled) }
    var sendSound by remember(revision) { mutableStateOf(vm.settings.sendSoundEnabled) }
    var receiveSound by remember(revision) { mutableStateOf(vm.settings.receiveSoundEnabled) }
    var pinnedEnabled by remember(revision) { mutableStateOf(vm.settings.pinnedEnabled) }
    var archivingEnabled by remember(revision) { mutableStateOf(vm.settings.archivingEnabled) }
    var unreadAtTop by remember(revision) { mutableStateOf(vm.settings.unreadAtTopEnabled) }
    var draftsEnabled by remember(revision) { mutableStateOf(vm.settings.draftsEnabled) }
    var forwardingEnabled by remember(revision) { mutableStateOf(vm.settings.forwardingEnabled) }
    var showSimIndicator by remember(revision) { mutableStateOf(vm.settings.showSimIndicator) }
    var periodicBackup by remember(revision) { mutableStateOf(vm.settings.periodicBackupEnabled) }
    var backupFolder by remember(revision) { mutableStateOf(vm.settings.backupTreeUri) }

    var sims by remember { mutableStateOf(emptyList<SimCard>()) }
    var pinMode by remember { mutableStateOf<PinDialogMode?>(null) }
    var pendingImportUri by remember { mutableStateOf<Uri?>(null) }
    var pendingImportMode by remember {
        mutableStateOf(com.anindra.messages.data.ImportMode.MERGE)
    }
    var pendingImportFormat by remember {
        mutableStateOf(com.anindra.messages.data.BackupFormat.LEGACY)
    }
    var pendingImportSource by remember { mutableStateOf(ImportSource.OWN_BACKUP) }
    var pinInput by remember { mutableStateOf("") }
    var pinConfirm by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var showRawBackupWarning by remember { mutableStateOf(false) }

    val notificationSoundOptions = listOf(
        SettingsStore.NOTIFY_SOUND_DEFAULT to context.getString(R.string.settings_sound_default),
        SettingsStore.NOTIFY_SOUND_APP to context.getString(R.string.settings_sound_classic),
        SettingsStore.NOTIFY_SOUND_DRAGON to context.getString(R.string.settings_sound_dragon),
        SettingsStore.NOTIFY_SOUND_UNIVERSFIELD_09 to context.getString(R.string.settings_sound_chime),
        SettingsStore.NOTIFY_SOUND_UNIVERSFIELD_062 to context.getString(R.string.settings_sound_bubble)
    )
    val themeMode = vm.themeMode

    LaunchedEffect(Unit) {
        sims = SimCards.load(context)
        val effective = SimSelection.effective(vm.settings.simSubscriptionId, sims)
        if (effective != vm.settings.simSubscriptionId) vm.settings.simSubscriptionId = effective
    }

    val simPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            sims = SimCards.load(context)
            simDialog = true
        }
    }

    val showImportResult: (com.anindra.messages.data.Repository.ImportResult) -> Unit = { result ->
        val msg = when (result) {
            is com.anindra.messages.data.Repository.ImportResult.Success ->
                if (result.merged != null) String.format(context.getString(R.string.settings_restored_msg), result.merged)
                else context.getString(R.string.settings_backup_restored)
            is com.anindra.messages.data.Repository.ImportResult.Error ->
                String.format(context.getString(R.string.settings_import_failed), result.message)
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            vm.peekBackupFormat(it) { format ->
                pendingImportUri = it
                pendingImportFormat = format
                pendingImportSource = ImportSource.OWN_BACKUP
                importModeDialog = true
            }
        }
    }

    val smsIeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            pendingImportUri = it
            pendingImportSource = ImportSource.SMS_IE
            importModeDialog = true
        }
    }

    val backupFolderLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                vm.settings.backupTreeUri = uri.toString()
                backupFolder = uri.toString()
            } catch (_: SecurityException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.settings_backup_location_failed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    var profileAvatarTrigger by remember { mutableIntStateOf(0) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val ok = CustomAvatarStore.saveAvatar(context, "me", uri)
            if (ok) {
                invalidateAvatarCache("me")
                profileAvatarTrigger++
                Toast.makeText(context, "تصویر پروفایل شما ذخیره شد", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val isPersian = vm.settings.appLanguage == "fa" || (vm.settings.appLanguage == "system" && java.util.Locale.getDefault().language == "fa")
    // Resolve labels
    val themeSubtitle = themeLabel(themeMode, context)
    val vipSubtitle = if (vm.settings.vipThemesUnlocked) {
        if (isPersian) "فعال شده 🔓✨ (۱۰ پوسته استثنایی)" else "Unlocked 🔓✨ (10 Exclusive Themes)"
    } else {
        if (isPersian) "قفل شده 🔒 (جهت فعال‌سازی لمس کنید)" else "Locked 🔒 (Tap to enter security token)"
    }
    val langSubtitle = when (vm.settings.appLanguage) {
        "fa" -> stringResource(R.string.language_fa)
        "en" -> stringResource(R.string.language_en)
        else -> stringResource(R.string.language_system)
    }
    val calendarSubtitle = when (vm.settings.calendarType) {
        SettingsStore.CALENDAR_GREGORIAN -> "میلادی (گرگوریان)"
        else -> "شمسی (جلالی) - پیش‌فرض"
    }
    val linesSubtitle = when (vm.settings.previewLines) {
        1 -> stringResource(R.string.preview_lines_1)
        2 -> stringResource(R.string.preview_lines_2)
        3 -> stringResource(R.string.preview_lines_3)
        else -> "${vm.settings.previewLines} lines"
    }
    val cellColorSubtitle = when (vm.settings.customCellColor) {
        "green" -> stringResource(R.string.cell_color_green)
        "cyan" -> stringResource(R.string.cell_color_cyan)
        "gold" -> stringResource(R.string.cell_color_gold)
        "red" -> stringResource(R.string.cell_color_red)
        "purple" -> stringResource(R.string.cell_color_purple)
        "silver" -> stringResource(R.string.cell_color_silver)
        else -> stringResource(R.string.cell_color_theme)
    }
    val cellStyleSubtitle = when (vm.settings.cellStyle) {
        "divider" -> stringResource(R.string.cell_style_divider)
        else -> stringResource(R.string.cell_style_card)
    }
    val borderOpacitySubtitle = "${(vm.settings.cellBorderOpacityFloat * 100).toInt()}%"
    val borderWidthSubtitle = String.format(java.util.Locale.US, "%.1f dp", vm.settings.cellBorderWidthFloat)
    val messageCardShapeSubtitle = when (vm.settings.messageCardShape) {
        SettingsStore.SHAPE_CYBER -> "Cyberpunk (4dp)"
        SettingsStore.SHAPE_BUBBLE -> "Classic Bubble"
        SettingsStore.SHAPE_MINIMAL -> "Minimal (2dp)"
        SettingsStore.SHAPE_CAPSULE -> "Capsule (22dp)"
        else -> "Rounded (12dp)"
    }
    val dividerColorSubtitle = when (vm.settings.dividerColor) {
        "gold" -> "طلایی استارک (Stark Gold)"
        "green" -> "سبز ماتریکس (Matrix Green)"
        "cyan" -> "آبی سایان (Cyber Cyan)"
        "silver" -> "نقره‌ای (Ghost Silver)"
        "subtle" -> "خاکستری مات (Subtle Dim)"
        else -> "هماهنگ با تم انتخابی"
    }
    val launcherIconSubtitle = when (vm.settings.launcherIcon) {
        SettingsStore.ICON_STARK_GOLD -> stringResource(R.string.launcher_icon_stark_gold)
        SettingsStore.ICON_MATRIX_GREEN -> stringResource(R.string.launcher_icon_matrix_green)
        SettingsStore.ICON_CYBER_CYAN -> stringResource(R.string.launcher_icon_cyber_cyan)
        else -> stringResource(R.string.launcher_icon_black_white)
    }
    val selectedSub = sims.firstOrNull { it.subscriptionId == vm.settings.simSubscriptionId }
    val currentSimLabel = if (selectedSub == null) {
        context.getString(R.string.sim_default)
    } else {
        when (val label = SimLabels.resolve(
            selectedSub.subscriptionId, selectedSub.slotIndex, selectedSub.carrierName
        )) {
            SimLabel.Default -> context.getString(R.string.sim_default)
            is SimLabel.Carrier -> String.format(
                context.getString(R.string.settings_sim_label_format), label.carrier, label.slot
            )
            is SimLabel.Slot -> String.format(
                context.getString(R.string.settings_sim_label), label.slot
            )
            SimLabel.Unknown -> context.getString(R.string.sim_default)
        }
    }
    val swipeLeftSubtitle = swipeActionLabel(vm.settings.swipeLeftAction, context)
    val swipeRightSubtitle = swipeActionLabel(vm.settings.swipeRightAction, context)

    // Construct the complete searchable catalog
    val allSearchableItems = remember(
        revision, delayedSending, delaySeconds, deliveryReports, sendSound, receiveSound,
        pinnedEnabled, archivingEnabled, unreadAtTop, draftsEnabled, forwardingEnabled,
        showSimIndicator, hideNotifBody, privacyMode, appLock, periodicBackup, currentSimLabel,
        themeSubtitle, langSubtitle, linesSubtitle, cellColorSubtitle, cellStyleSubtitle,
        borderOpacitySubtitle, dividerColorSubtitle, launcherIconSubtitle, swipeLeftSubtitle,
        swipeRightSubtitle, scheduledCount
    ) {
        listOf(
            // Appearance & Theme
            SearchSettingItem(
                id = "theme",
                title = context.getString(R.string.settings_theme_title),
                subtitle = themeSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("theme", "تم", "پوسته", "رنگ", "ظاهر", "dark", "light", "amoled", "stark", "matrix", "arctic", "ghost", "crimson", "cyber", "quantum", "blackout", "gold", "cyan", "green"),
                onClick = { themeDialog = true }
            ),
            SearchSettingItem(
                id = "vip_themes",
                title = if (isPersian) "تم‌های اختصاصی هکری VIP 💎" else "VIP Exclusive Hacker Themes 💎",
                subtitle = vipSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("vip", "وی آی پی", "توکن", "هکری", "تم اختصاصی", "تم روشن", "تم تاریک", "light theme", "dark theme", "cyber", "phantom", "matrix", "stark", "sunset", "arctic", "blood", "synthwave", "titanium"),
                onClick = {
                    if (vm.settings.vipThemesUnlocked) {
                        vipThemesDialog = true
                    } else {
                        tokenInput = ""
                        tokenError = false
                        vipTokenDialog = true
                    }
                }
            ),
            SearchSettingItem(
                id = "cell_style",
                title = context.getString(R.string.settings_cell_style_title),
                subtitle = cellStyleSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("cell", "style", "card", "divider", "کادر", "سلول", "دیوایدر", "کارت", "خط جداکننده", "جداساز", "حذف کادر"),
                onClick = { cellStyleDialog = true }
            ),
            SearchSettingItem(
                id = "border_opacity",
                title = context.getString(R.string.settings_border_opacity_title),
                subtitle = borderOpacitySubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("border", "opacity", "intensity", "شفافیت", "شدت", "کمرنگ", "پررنگ", "نئون", "کادر", "روشنایی"),
                onClick = { cellOpacityDialog = true }
            ),
            SearchSettingItem(
                id = "divider_color",
                title = context.getString(R.string.settings_divider_color_title),
                subtitle = dividerColorSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("divider", "color", "رنگ خط", "دیوایدر", "خط جداکننده", "رنگ دیوایدر"),
                onClick = { dividerColorDialog = true }
            ),
            SearchSettingItem(
                id = "cell_color",
                title = context.getString(R.string.settings_cell_color_title),
                subtitle = cellColorSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("cell color", "color", "رنگ کادر", "رنگ سلول", "رنگ پیش‌فرض", "کادر چت"),
                onClick = { cellColorDialog = true }
            ),
            SearchSettingItem(
                id = "preview_lines",
                title = context.getString(R.string.settings_preview_lines_title),
                subtitle = linesSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("preview", "lines", "خطوط", "پیش‌نمایش", "ارتفاع", "متن پیام", "نمایش پیام"),
                onClick = { previewLinesDialog = true }
            ),
            SearchSettingItem(
                id = "launcher_icon",
                title = context.getString(R.string.settings_launcher_icon_title),
                subtitle = launcherIconSubtitle,
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("icon", "launcher", "آیکون", "لوگو", "تصویر برنامه", "badge", "آیکون لانچر", "مشکی", "سفید"),
                onClick = { launcherIconDialog = true }
            ),
            SearchSettingItem(
                id = "profile_avatar",
                title = "تصویر پروفایل شما (A/SMS)",
                subtitle = "انتخاب تصویر دلخواه برای پروفایل کاربری",
                category = "ظاهر و طراحی / Appearance",
                keywords = listOf("avatar", "profile", "عکس", "پروفایل", "تصویر", "photo", "camera", "تصویر کاربری"),
                onClick = {
                    photoPickerLauncher.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ),

            // Messaging & Delivery
            SearchSettingItem(
                id = "delayed_sending",
                title = context.getString(R.string.settings_delayed_title),
                subtitle = context.getString(R.string.settings_delayed_subtitle),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("delay", "send", "تاخیر", "ارسال با تاخیر", "وقفه", "لغو ارسال", "wait", "delay send", "ثانیه"),
                isSwitch = true,
                checked = delayedSending,
                onChecked = {
                    delayedSending = it
                    vm.settings.delayedSendingEnabled = it
                }
            ),
            SearchSettingItem(
                id = "delay_seconds",
                title = context.getString(R.string.settings_delay_secs_title),
                subtitle = String.format(context.getString(R.string.settings_delay_secs_format), delaySeconds),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("delay", "seconds", "ثانیه", "مدت تاخیر", "زمان تاخیر"),
                onClick = { delaySecondsDialog = true }
            ),
            SearchSettingItem(
                id = "delivery_reports",
                title = context.getString(R.string.settings_delivery_reports_title),
                subtitle = context.getString(R.string.settings_delivery_reports_subtitle),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("delivery", "report", "تحویل", "گزارش تحویل", "دلیوری", "رسید ارسال"),
                isSwitch = true,
                checked = deliveryReports,
                onChecked = {
                    deliveryReports = it
                    vm.settings.deliveryReportsEnabled = it
                }
            ),
            SearchSettingItem(
                id = "pin_sim",
                title = context.getString(R.string.settings_pin_sim_card),
                subtitle = currentSimLabel,
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("sim", "سیم‌کارت", "سیم", "اپراتور", "carrier", "dual sim", "سیم ۱", "سیم ۲"),
                onClick = {
                    val hasPerm = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED
                    if (hasPerm) {
                        sims = SimCards.load(context)
                        simDialog = true
                    } else {
                        simPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                    }
                }
            ),
            SearchSettingItem(
                id = "sim_indicator",
                title = context.getString(R.string.settings_sim_indicator),
                subtitle = context.getString(R.string.settings_sim_indicator_desc),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("sim indicator", "نشانگر سیم‌کارت", "برچسب سیم", "نمایش سیم‌کارت"),
                isSwitch = true,
                checked = showSimIndicator,
                onChecked = {
                    showSimIndicator = it
                    vm.settings.showSimIndicator = it
                }
            ),
            SearchSettingItem(
                id = "drafts",
                title = context.getString(R.string.settings_drafts_title),
                subtitle = context.getString(R.string.settings_drafts_subtitle),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("draft", "پیش‌نویس", "ذخیره متن", "متن نیمه‌کاره"),
                isSwitch = true,
                checked = draftsEnabled,
                onChecked = {
                    draftsEnabled = it
                    vm.settings.draftsEnabled = it
                }
            ),
            SearchSettingItem(
                id = "forwarding",
                title = context.getString(R.string.settings_forwarding_title),
                subtitle = context.getString(R.string.settings_forwarding_subtitle),
                category = "ارسال و دریافت / Messaging",
                keywords = listOf("forward", "بازارسال", "فوروارد", "ارسال مجدد"),
                isSwitch = true,
                checked = forwardingEnabled,
                onChecked = {
                    forwardingEnabled = it
                    vm.settings.forwardingEnabled = it
                }
            ),

            // Notifications & Sounds
            SearchSettingItem(
                id = "notif_sound",
                title = context.getString(R.string.settings_pin_notification_sound),
                subtitle = notificationSoundLabel(notificationSound, notificationSoundOptions, context),
                category = "اعلان‌ها و صداها / Notifications",
                keywords = listOf("sound", "ringtone", "صدا", "زنگ", "اعلان", "صدای پیامک", "دریافت", "نوتیفیکیشن", "آهنگ"),
                onClick = { soundDialog = true }
            ),
            SearchSettingItem(
                id = "send_sound",
                title = context.getString(R.string.settings_send_sound_title),
                subtitle = context.getString(R.string.settings_send_sound_subtitle),
                category = "اعلان‌ها و صداها / Notifications",
                keywords = listOf("send sound", "صدای ارسال", "ارسال", "beep", "صدای بوق"),
                isSwitch = true,
                checked = sendSound,
                onChecked = {
                    sendSound = it
                    vm.settings.sendSoundEnabled = it
                }
            ),
            SearchSettingItem(
                id = "receive_sound",
                title = context.getString(R.string.settings_receive_sound_title),
                subtitle = context.getString(R.string.settings_receive_sound_subtitle),
                category = "اعلان‌ها و صداها / Notifications",
                keywords = listOf("receive sound", "صدای دریافت", "دریافت در برنامه", "صدا"),
                isSwitch = true,
                checked = receiveSound,
                onChecked = {
                    receiveSound = it
                    vm.settings.receiveSoundEnabled = it
                }
            ),
            SearchSettingItem(
                id = "hide_notif_body",
                title = context.getString(R.string.settings_notif_hide_body_title),
                subtitle = context.getString(R.string.settings_notif_hide_body_subtitle),
                category = "اعلان‌ها و صداها / Notifications",
                keywords = listOf("hide", "private", "مخفی", "متن اعلان", "حریم خصوصی", "اعلان خصوصی", "notification body"),
                isSwitch = true,
                checked = hideNotifBody,
                onChecked = {
                    hideNotifBody = it
                    vm.settings.hideNotificationBody = it
                }
            ),
            SearchSettingItem(
                id = "system_notif",
                title = context.getString(R.string.settings_notif_title),
                subtitle = "تنظیمات سیستمی اعلان‌ها در اندروید",
                category = "اعلان‌ها و صداها / Notifications",
                keywords = listOf("system notification", "سیستم اعلان", "تنظیمات اندروید", "کانال اعلان"),
                onClick = { openSystemNotificationSettings(context) }
            ),

            // Gestures & Chat List
            SearchSettingItem(
                id = "pinned_top",
                title = context.getString(R.string.settings_pinned_title),
                subtitle = context.getString(R.string.settings_pinned_subtitle),
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("pin", "پین", "سنجاق", "بالای لیست", "مخاطب پین شده"),
                isSwitch = true,
                checked = pinnedEnabled,
                onChecked = {
                    pinnedEnabled = it
                    vm.settings.pinnedEnabled = it
                }
            ),
            SearchSettingItem(
                id = "unread_top",
                title = context.getString(R.string.settings_unread_top_title),
                subtitle = context.getString(R.string.settings_unread_top_subtitle),
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("unread", "خوانده نشده", "بالای صفحه", "پیام جدید"),
                isSwitch = true,
                checked = unreadAtTop,
                onChecked = {
                    unreadAtTop = it
                    vm.settings.unreadAtTopEnabled = it
                }
            ),
            SearchSettingItem(
                id = "swipe_left",
                title = context.getString(R.string.swipe_left_title),
                subtitle = swipeLeftSubtitle,
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("swipe", "left", "سوایپ", "کشیدن به چپ", "بایگانی", "حذف"),
                onClick = { swipeLeftDialog = true }
            ),
            SearchSettingItem(
                id = "swipe_right",
                title = context.getString(R.string.swipe_right_title),
                subtitle = swipeRightSubtitle,
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("swipe", "right", "سوایپ", "کشیدن به راست", "بایگانی", "حذف"),
                onClick = { swipeRightDialog = true }
            ),
            SearchSettingItem(
                id = "archiving",
                title = context.getString(R.string.settings_archiving_title),
                subtitle = context.getString(R.string.settings_archiving_subtitle),
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("archive", "آرشیو", "بایگانی", "مخفی کردن چت"),
                isSwitch = true,
                checked = archivingEnabled,
                onChecked = {
                    archivingEnabled = it
                    vm.settings.archivingEnabled = it
                }
            ),
            SearchSettingItem(
                id = "mark_read",
                title = context.getString(R.string.settings_mark_read_title),
                subtitle = context.getString(R.string.settings_mark_read_subtitle),
                category = "حرکات و لیست / List & Gestures",
                keywords = listOf("read", "خوانده شده", "علامت‌گذاری", "پاک کردن اعلان"),
                onClick = {
                    vm.markAllRead()
                    Toast.makeText(context, context.getString(R.string.settings_mark_read), Toast.LENGTH_SHORT).show()
                }
            ),

            // Security, Privacy & Retention
            SearchSettingItem(
                id = "app_lock",
                title = context.getString(R.string.settings_applock_title),
                subtitle = context.getString(R.string.settings_applock_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("lock", "fingerprint", "قفل", "اثر انگشت", "پین", "بیومتریک", "رمز"),
                isSwitch = true,
                checked = appLock,
                onChecked = { enable ->
                    verifyForAppLockChange(context) { verdict ->
                        when (verdict) {
                            AppLockVerdict.Verified -> {
                                appLock = enable
                                vm.settings.appLockEnabled = enable
                            }
                            AppLockVerdict.NoScreenLock -> Toast.makeText(
                                context,
                                context.getString(R.string.lock_setup_needed),
                                Toast.LENGTH_LONG
                            ).show()
                            AppLockVerdict.Rejected -> Unit
                        }
                    }
                }
            ),
            SearchSettingItem(
                id = "privacy_mode",
                title = context.getString(R.string.settings_privacy_title),
                subtitle = context.getString(R.string.settings_privacy_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("privacy", "حریم خصوصی", "اسکرین‌شات", "ضد ضبط", "screenshot"),
                isSwitch = true,
                checked = privacyMode,
                onChecked = {
                    privacyMode = it
                    (context as? Activity)?.let { act -> vm.setPrivacyMode(act, it) }
                }
            ),
            SearchSettingItem(
                id = "spam_blocked",
                title = context.getString(R.string.conversations_spam_blocked),
                subtitle = context.getString(R.string.settings_spam_blocked_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("block", "spam", "مسدود", "اسپم", "لیست سیاه", "شماره‌های مسدود"),
                onClick = onOpenSpamBlocked
            ),
            SearchSettingItem(
                id = "backup",
                title = context.getString(R.string.settings_backup_title),
                subtitle = context.getString(R.string.settings_backup_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("backup", "پشتیبان", "نسخه پشتیبان", "بکاپ", "رمزگذاری"),
                onClick = {
                    pinMode = PinDialogMode.SET
                    pinInput = ""
                    pinConfirm = ""
                    pinError = null
                }
            ),
            SearchSettingItem(
                id = "periodic_backup",
                title = context.getString(R.string.settings_periodic_backup_title),
                subtitle = "پشتیبان‌گیری منظم روزانه یا هفتگی",
                category = "امنیت و نگهداری / Security",
                keywords = listOf("periodic", "دوره‌ای", "خودکار", "پشتیبان خودکار", "روزانه", "هفتگی"),
                isSwitch = true,
                checked = periodicBackup,
                onChecked = { enabled ->
                    periodicBackup = enabled
                    vm.settings.periodicBackupEnabled = enabled
                    PeriodicBackupScheduler.apply(context, enabled, vm.settings.periodicBackupInterval)
                }
            ),
            SearchSettingItem(
                id = "import_backup",
                title = context.getString(R.string.settings_import_title),
                subtitle = context.getString(R.string.settings_import_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("restore", "import", "بازیابی", "ایمپورت", "sms backup and restore", "بازگردانی"),
                onClick = { importSourceDialog = true }
            ),
            SearchSettingItem(
                id = "trash",
                title = context.getString(R.string.settings_trash_title),
                subtitle = context.getString(R.string.settings_trash_subtitle),
                category = "امنیت و نگهداری / Security",
                keywords = listOf("trash", "سطل زباله", "حذف شده", "بازیابی حذف"),
                onClick = onOpenTrash
            ),
            SearchSettingItem(
                id = "scheduled_messages",
                title = context.getString(R.string.settings_scheduled_title),
                subtitle = scheduledCount ?: "مشاهده و مدیریت پیام‌های زمان‌بندی‌شده",
                category = "امنیت و نگهداری / Security",
                keywords = listOf("scheduled", "زمان‌بندی", "ارسال در آینده", "زمان‌بندی شده"),
                onClick = onOpenScheduled
            ),

            // General & System
            SearchSettingItem(
                id = "language",
                title = context.getString(R.string.settings_language_title),
                subtitle = langSubtitle,
                category = "عمومی و سیستم / General",
                keywords = listOf("language", "زبان", "فارسی", "english", "انگلیسی", "persian", "مترجم"),
                onClick = { languageDialog = true }
            ),
            SearchSettingItem(
                id = "calendar",
                title = "نوع تقویم / Calendar System",
                subtitle = calendarSubtitle,
                category = "عمومی و سیستم / General",
                keywords = listOf("calendar", "تقویم", "شمسی", "میلادی", "جلالی", "تاریخ", "solar", "jalali", "gregorian"),
                onClick = { calendarDialog = true }
            ),
            SearchSettingItem(
                id = "advanced",
                title = context.getString(R.string.settings_advanced_title),
                subtitle = context.getString(R.string.settings_advanced_subtitle),
                category = "عمومی و سیستم / General",
                keywords = listOf("advanced", "پیشرفته", "مهندسی", "مخاطبین", "mms", "دیباگ", "diagnostics", "عیب‌یابی", "فونت"),
                onClick = onOpenAdvanced
            )
        )
    }

    // Filter matching items
    val searchResults = remember(searchQuery, allSearchableItems) {
        if (searchQuery.isBlank()) emptyList()
        else {
            val q = searchQuery.trim().lowercase()
                .replace('ي', 'ی')
                .replace('ك', 'ک')
                .replace('آ', 'ا')
            val words = q.split(" ").filter { it.isNotBlank() }
            allSearchableItems.filter { item ->
                val pool = (item.title + " " + (item.subtitle ?: "") + " " + item.category + " " + item.keywords.joinToString(" "))
                    .lowercase()
                    .replace('ي', 'ی')
                    .replace('ك', 'ک')
                    .replace('آ', 'ا')
                words.all { pool.contains(it) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searching) {
                        LaunchedEffect(Unit) { focusRequester.requestFocus() }
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(stringResource(R.string.settings_search_hint)) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                        )
                    } else {
                        Text(stringResource(R.string.settings_general))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (searching) {
                            searching = false
                            searchQuery = ""
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(if (searching) R.string.icon_close_search else R.string.icon_back)
                        )
                    }
                },
                actions = {
                    if (!searching) {
                        IconButton(onClick = { searching = true }) {
                            Icon(Icons.Outlined.Search, stringResource(R.string.icon_search))
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = SettingsLayout.SCREEN_PADDING)
        ) {
            Spacer(Modifier.height(SettingsLayout.TOP_GAP))

            if (searching && searchQuery.isNotBlank()) {
                // Search Results Mode
                Text(
                    text = "${searchResults.size} مورد یافت شد",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (searchResults.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_search_none),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    SettingsGroup {
                        searchResults.forEachIndexed { index, item ->
                            val pos = when {
                                searchResults.size == 1 -> RowPosition.SINGLE
                                index == 0 -> RowPosition.FIRST
                                index == searchResults.size - 1 -> RowPosition.LAST
                                else -> RowPosition.MIDDLE
                            }
                            SettingsRow(
                                position = pos,
                                title = item.title,
                                subtitle = item.subtitle,
                                checked = if (item.isSwitch) item.checked else null,
                                onChecked = item.onChecked,
                                onClick = item.onClick
                            )
                        }
                    }
                }
            } else {
                // Normal Grouped Settings Mode

                // User Profile Picture HUD Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF09101C)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66FFCC00)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable {
                            photoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Box {
                            key(profileAvatarTrigger) {
                                PersonAvatar(
                                    key = "me",
                                    size = 52.dp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0xFFFFCC00), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CameraAlt,
                                    contentDescription = null,
                                    tint = Color(0xFF04070B),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "تصویر پروفایل شما (A/SMS)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFF2B2)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "برای انتخاب یا تغییر عکس ضربه بزنید",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF88A0B8)
                            )
                        }
                    }
                }

                // Section 1: Appearance & Theme
                SettingsSectionHeader("ظاهر، پوسته و سلول‌های چت")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_theme_title),
                        subtitle = themeSubtitle,
                        onClick = { themeDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = if (isPersian) "تم‌های اختصاصی هکری VIP 💎" else "VIP Exclusive Hacker Themes 💎",
                        subtitle = vipSubtitle,
                        onClick = {
                            if (vm.settings.vipThemesUnlocked) {
                                vipThemesDialog = true
                            } else {
                                tokenInput = ""
                                tokenError = false
                                vipTokenDialog = true
                            }
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_cell_style_title),
                        subtitle = cellStyleSubtitle,
                        onClick = { cellStyleDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_border_opacity_slider_title),
                        subtitle = borderOpacitySubtitle,
                        onClick = { borderOpacitySliderDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_border_width_slider_title),
                        subtitle = borderWidthSubtitle,
                        onClick = { borderWidthSliderDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_message_card_shape_title),
                        subtitle = messageCardShapeSubtitle,
                        onClick = { messageCardShapeDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_divider_color_title),
                        subtitle = dividerColorSubtitle,
                        onClick = { dividerColorDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_cell_color_title),
                        subtitle = cellColorSubtitle,
                        onClick = { cellColorDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_preview_lines_title),
                        subtitle = linesSubtitle,
                        onClick = { previewLinesDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_launcher_icon_title),
                        subtitle = launcherIconSubtitle,
                        onClick = { launcherIconDialog = true }
                    )
                }

                // Section 2: Messaging & Delivery
                SettingsSectionHeader("ارسال و دریافت پیام")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_delayed_title),
                        subtitle = stringResource(R.string.settings_delayed_subtitle),
                        checked = delayedSending,
                        onChecked = {
                            delayedSending = it
                            vm.settings.delayedSendingEnabled = it
                        }
                    )
                    if (delayedSending) {
                        SettingsRow(
                            position = RowPosition.MIDDLE,
                            title = stringResource(R.string.settings_delay_secs_title),
                            subtitle = String.format(stringResource(R.string.settings_delay_secs_format), delaySeconds),
                            onClick = { delaySecondsDialog = true }
                        )
                    }
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_delivery_reports_title),
                        subtitle = stringResource(R.string.settings_delivery_reports_subtitle),
                        checked = deliveryReports,
                        onChecked = {
                            deliveryReports = it
                            vm.settings.deliveryReportsEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_pin_sim_card),
                        subtitle = currentSimLabel,
                        onClick = {
                            val hasPerm = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                                PackageManager.PERMISSION_GRANTED
                            if (hasPerm) {
                                sims = SimCards.load(context)
                                simDialog = true
                            } else {
                                simPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                            }
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_sim_indicator),
                        subtitle = stringResource(R.string.settings_sim_indicator_desc),
                        checked = showSimIndicator,
                        onChecked = {
                            showSimIndicator = it
                            vm.settings.showSimIndicator = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_drafts_title),
                        subtitle = stringResource(R.string.settings_drafts_subtitle),
                        checked = draftsEnabled,
                        onChecked = {
                            draftsEnabled = it
                            vm.settings.draftsEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_forwarding_title),
                        subtitle = stringResource(R.string.settings_forwarding_subtitle),
                        checked = forwardingEnabled,
                        onChecked = {
                            forwardingEnabled = it
                            vm.settings.forwardingEnabled = it
                        }
                    )
                }

                // Section 3: Notifications & Sounds
                SettingsSectionHeader("اعلان‌ها و صداها")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_pin_notification_sound),
                        subtitle = notificationSoundLabel(notificationSound, notificationSoundOptions, context),
                        onClick = { soundDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_send_sound_title),
                        subtitle = stringResource(R.string.settings_send_sound_subtitle),
                        checked = sendSound,
                        onChecked = {
                            sendSound = it
                            vm.settings.sendSoundEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_receive_sound_title),
                        subtitle = stringResource(R.string.settings_receive_sound_subtitle),
                        checked = receiveSound,
                        onChecked = {
                            receiveSound = it
                            vm.settings.receiveSoundEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_notif_hide_body_title),
                        subtitle = stringResource(R.string.settings_notif_hide_body_subtitle),
                        checked = hideNotifBody,
                        onChecked = {
                            hideNotifBody = it
                            vm.settings.hideNotificationBody = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_notif_title),
                        subtitle = "تنظیمات سیستمی اعلان‌ها در اندروید",
                        onClick = { openSystemNotificationSettings(context) }
                    )
                }

                // Section 4: Gestures & Chat List
                SettingsSectionHeader("حرکات لمسی و لیست گفتگوها")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_pinned_title),
                        subtitle = stringResource(R.string.settings_pinned_subtitle),
                        checked = pinnedEnabled,
                        onChecked = {
                            pinnedEnabled = it
                            vm.settings.pinnedEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_unread_top_title),
                        subtitle = stringResource(R.string.settings_unread_top_subtitle),
                        checked = unreadAtTop,
                        onChecked = {
                            unreadAtTop = it
                            vm.settings.unreadAtTopEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.swipe_left_title),
                        subtitle = swipeLeftSubtitle,
                        onClick = { swipeLeftDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.swipe_right_title),
                        subtitle = swipeRightSubtitle,
                        onClick = { swipeRightDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_archiving_title),
                        subtitle = stringResource(R.string.settings_archiving_subtitle),
                        checked = archivingEnabled,
                        onChecked = {
                            archivingEnabled = it
                            vm.settings.archivingEnabled = it
                        }
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_mark_read_title),
                        subtitle = stringResource(R.string.settings_mark_read_subtitle),
                        onClick = {
                            vm.markAllRead()
                            Toast.makeText(context, context.getString(R.string.settings_mark_read), Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Section 5: Security, Backup & Retention
                SettingsSectionHeader("امنیت، پشتیبان‌گیری و نگهداری")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_applock_title),
                        subtitle = stringResource(R.string.settings_applock_subtitle),
                        checked = appLock,
                        onChecked = { enable ->
                            verifyForAppLockChange(context) { verdict ->
                                when (verdict) {
                                    AppLockVerdict.Verified -> {
                                        appLock = enable
                                        vm.settings.appLockEnabled = enable
                                    }
                                    AppLockVerdict.NoScreenLock -> Toast.makeText(
                                        context,
                                        context.getString(R.string.lock_setup_needed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    AppLockVerdict.Rejected -> Unit
                                }
                            }
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_privacy_title),
                        subtitle = stringResource(R.string.settings_privacy_subtitle),
                        checked = privacyMode,
                        onChecked = {
                            privacyMode = it
                            (context as? Activity)?.let { act -> vm.setPrivacyMode(act, it) }
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.conversations_spam_blocked),
                        subtitle = stringResource(R.string.settings_spam_blocked_subtitle),
                        onClick = onOpenSpamBlocked
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_backup_title),
                        subtitle = stringResource(R.string.settings_backup_subtitle),
                        enabled = !privacyMode,
                        onClick = {
                            pinMode = PinDialogMode.SET
                            pinInput = ""
                            pinConfirm = ""
                            pinError = null
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_periodic_backup_title),
                        subtitle = "پشتیبان‌گیری منظم روزانه یا هفتگی",
                        checked = periodicBackup,
                        onChecked = { enabled ->
                            periodicBackup = enabled
                            vm.settings.periodicBackupEnabled = enabled
                            PeriodicBackupScheduler.apply(context, enabled, vm.settings.periodicBackupInterval)
                        }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_import_title),
                        subtitle = stringResource(R.string.settings_import_subtitle),
                        onClick = { importSourceDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = stringResource(R.string.settings_trash_title),
                        subtitle = stringResource(R.string.settings_trash_subtitle),
                        onClick = onOpenTrash
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_scheduled_title),
                        subtitle = scheduledCount ?: "مدیریت پیام‌های زمان‌بندی‌شده",
                        onClick = onOpenScheduled
                    )
                }

                // Section 6: General & Advanced
                SettingsSectionHeader("عمومی و پیشرفته")
                SettingsGroup {
                    SettingsRow(
                        position = RowPosition.FIRST,
                        title = stringResource(R.string.settings_language_title),
                        subtitle = langSubtitle,
                        onClick = { languageDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.MIDDLE,
                        title = "نوع تقویم",
                        subtitle = calendarSubtitle,
                        onClick = { calendarDialog = true }
                    )
                    SettingsRow(
                        position = RowPosition.LAST,
                        title = stringResource(R.string.settings_advanced_title),
                        subtitle = stringResource(R.string.settings_advanced_subtitle),
                        onClick = onOpenAdvanced
                    )
                }
            }

            SettingsFooter(stringResource(R.string.messages_footer))
        }
    }

    // Dialogs
    if (themeDialog) {
        AlertDialog(
            onDismissRequest = { themeDialog = false },
            title = { Text(stringResource(R.string.settings_choose_theme)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val options = buildList {
                        add("stark" to "Stark Cyber HUD (Iron Man Gold & Cockpit Black)")
                        add("cyber" to "AB Cyber Blue (Electric Blue / Cyan)")
                        add("blackout" to "Blackout (Pure Black OLED)")
                        add("matrix" to "Neon Matrix (Black & Green)")
                        add("arctic" to "Arctic Glass (Dark Blue & Cyan)")
                        add("ghost" to "Ghost Protocol (Black & Silver)")
                        add("ocean" to "Deep Ocean (Navy & Electric Blue)")
                        add("crimson" to "Crimson Hacker (Black & Red)")
                        add("quantum" to "Quantum (Cyber Purple & Cyan)")
                        add("orange" to "Ù†Ø§Ø±Ù†Ø¬ÛŒ Ø³Ø§ÛŒØ¨Ø±Ù¾Ø§Ù†Ú© / Cyber Orange")
                        add("minimal" to "Minimal Glass (Subtle Transparent)")
                        add("light" to stringResource(R.string.settings_theme_light))
                        add("system" to stringResource(R.string.settings_theme_system))
                        if (vm.settings.vipThemesUnlocked) {
                            add("vip_stark_clean" to "ðŸ’Ž VIP: Stark Clean Tech (Ø±ÙˆØ´Ù†)")
                            add("vip_matrix_light" to "ðŸ’Ž VIP: Neo Matrix Light (Ø±ÙˆØ´Ù†)")
                            add("vip_sunset_amber" to "ðŸ’Ž VIP: Solar Amber Sunset (Ø±ÙˆØ´Ù†)")
                            add("vip_quantum_lilac" to "ðŸ’Ž VIP: Quantum Vaporwave (Ø±ÙˆØ´Ù†)")
                            add("vip_arctic_ice" to "ðŸ’Ž VIP: Arctic Glacial Ice (Ø±ÙˆØ´Ù†)")
                            add("vip_phantom_hacker" to "ðŸ’Ž VIP: Phantom Hacker (Ù…Ø´Ú©ÛŒ/Ù†Ø¹Ù†Ø§ÛŒÛŒ)")
                            add("vip_blood_overdrive" to "ðŸ’Ž VIP: Blood Overdrive (Ù…Ø´Ú©ÛŒ/Ù‚Ø±Ù…Ø²)")
                            add("vip_synthwave_2077" to "ðŸ’Ž VIP: Synthwave 2077 (ØµÙˆØ±ØªÛŒ/Ø³Ø§ÛŒØ§Ù†)")
                            add("vip_titanium_monolith" to "ðŸ’Ž VIP: Titanium Monolith (Ú¯Ø±Ø§ÙÛŒØª)")
                            add("vip_matrix_terminal_zero" to "ðŸ’Ž VIP: Matrix Terminal Zero (ÙØ³ÙØ±ÛŒ)")
                        }
                    }
                    options.forEach { (value, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vm.setTheme(value) }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = themeMode == value,
                                onClick = { vm.setTheme(value) }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { themeDialog = false }) { Text(stringResource(R.string.common_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { themeDialog = false }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (vipTokenDialog) {
        AlertDialog(
            onDismissRequest = { vipTokenDialog = false },
            title = {
                Text(
                    text = if (isPersian) "Ù‚ÙÙ„â€ŒÚ¯Ø´Ø§ÛŒÛŒ ØªÙ…â€ŒÙ‡Ø§ÛŒ Ø§Ø®ØªØµØ§ØµÛŒ VIP ðŸ”’" else "Unlock VIP Exclusive Themes ðŸ”’",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isPersian)
                            "Ø¨Ø±Ø§ÛŒ Ø¯Ø³ØªØ±Ø³ÛŒ Ø¯Ø§Ø¦Ù…ÛŒ Ø¨Ù‡ Ûµ ØªÙ… Ø±ÙˆØ´Ù† ÙÙˆÙ‚â€ŒØ§Ù„Ø¹Ø§Ø¯Ù‡ Ùˆ Ûµ ØªÙ… Ù…Ø´Ú©ÛŒ/Ù‡Ú©Ø±ÛŒ Ø´Ø§Ù‡Ú©Ø§Ø±ØŒ ØªÙˆÚ©Ù† Ø§Ù…Ù†ÛŒØªÛŒ Ø§Ø®ØªØµØ§ØµÛŒ Ø±Ø§ ÙˆØ§Ø±Ø¯ Ú©Ù†ÛŒØ¯:"
                        else
                            "Enter your security token to permanently unlock 5 extraordinary light themes and 5 masterpiece dark/hacker themes:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tokenInput,
                        onValueChange = {
                            tokenInput = it
                            tokenError = false
                        },
                        label = { Text(if (isPersian) "ØªÙˆÚ©Ù† Ø§Ù…Ù†ÛŒØªÛŒ" else "Security Token") },
                        singleLine = true,
                        isError = tokenError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (tokenError) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (isPersian) "ØªÙˆÚ©Ù† Ø§Ù…Ù†ÛŒØªÛŒ ÙˆØ§Ø±Ø¯Ø´Ø¯Ù‡ Ù†Ø§Ù…Ø¹ØªØ¨Ø± Ø§Ø³Øª!" else "Invalid security token!",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (vm.settings.unlockVipThemesWithToken(tokenInput)) {
                            vipTokenDialog = false
                            vipThemesDialog = true
                            Toast.makeText(
                                context,
                                if (isPersian) "ØªÙ…â€ŒÙ‡Ø§ÛŒ VIP Ø¨Ø§ Ù…ÙˆÙÙ‚ÛŒØª ÙØ¹Ø§Ù„ Ùˆ Ø¯Ø§Ø¦Ù…ÛŒ Ø´Ø¯Ù†Ø¯! ðŸ”“âœ¨" else "VIP Themes successfully unlocked! ðŸ”“âœ¨",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            tokenError = true
                        }
                    }
                ) {
                    Text(if (isPersian) "ØªØ£ÛŒÛŒØ¯ Ùˆ Ø¨Ø§Ø²Ú¯Ø´Ø§ÛŒÛŒ" else "Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = { vipTokenDialog = false }) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    if (vipThemesDialog) {
        AlertDialog(
            onDismissRequest = { vipThemesDialog = false },
            title = {
                Text(
                    text = if (isPersian) "ØªÙ…â€ŒÙ‡Ø§ÛŒ Ø§Ø®ØªØµØ§ØµÛŒ Ù‡Ú©Ø±ÛŒ VIP ðŸ’Ž" else "VIP Exclusive Hacker Themes ðŸ’Ž",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Group 1: 5 Extraordinary Light Themes
                    Text(
                        text = if (isPersian) "â˜€ï¸ Ûµ ØªÙ… Ø§Ø®ØªØµØ§ØµÛŒ Ø±ÙˆØ´Ù† Ùˆ Ù…Ø¯Ø±Ù† (Light Themes)" else "â˜€ï¸ 5 Exclusive Modern Light Themes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
                    )

                    val lightVipThemes = listOf(
                        Triple(
                            "vip_stark_clean",
                            if (isPersian) "Stark Clean Tech (Ø³ÙÛŒØ¯ / Ú©Ø¨Ø§Ù„Øª)" else "Stark Clean Tech (White & Cobalt)",
                            if (isPersian) "Ù¾Ø³â€ŒØ²Ù…ÛŒÙ†Ù‡ Ø³ÙÛŒØ¯ Ú©Ø±ÛŒØ³ØªØ§Ù„ÛŒØŒ Ø­Ø±ÙˆÙ Ú©Ø§Ù…Ù„Ø§Ù‹ Ù…Ø´Ú©ÛŒ Ùˆ Ú©Ø§Ø¯Ø±Ù‡Ø§ÛŒ Ø¢Ø¨ÛŒ Ù†Ø¦ÙˆÙ†ÛŒ" else "Pristine white tech paper, rich black contrast, vibrant cobalt borders"
                        ),
                        Triple(
                            "vip_matrix_light",
                            if (isPersian) "Neo Matrix Light (Ú©Ø§ØºØ° Ø¯ÛŒØ¬ÛŒØªØ§Ù„ / Ø³Ø¨Ø²)" else "Neo Matrix Light (Digital Paper & Emerald)",
                            if (isPersian) "Ù¾ÙˆØ³ØªÙ‡ Ø±ÙˆØ´Ù† Ù‡Ú©Ø±ÛŒ Ø¨Ø§ Ø®Ø·ÙˆØ· Ùˆ Ú©Ø§Ø¯Ø±Ù‡Ø§ÛŒ Ø³Ø¨Ø² Ø²Ù…Ø±Ø¯ÛŒ Ù…Ø§Øª Ùˆ ØªÙ…ÛŒØ²" else "Clean digital paper with emerald green matrix framing and dark accents"
                        ),
                        Triple(
                            "vip_sunset_amber",
                            if (isPersian) "Solar Amber Sunset (Ù…Ø±ÙˆØ§Ø±ÛŒØ¯ Ú¯Ø±Ù… / Ú©Ù‡Ø±Ø¨Ø§ÛŒÛŒ)" else "Solar Amber Sunset (Warm Solar & Amber)",
                            if (isPersian) "Ù¾ÙˆØ³ØªÙ‡ Ø®ÙˆØ±Ø´ÛŒØ¯ÛŒ Ù„ÙˆÚ©Ø³ Ø¨Ø§ Ø±Ù†Ú¯â€ŒÙ‡Ø§ÛŒ Ú©Ù‡Ø±Ø¨Ø§ÛŒÛŒØŒ Ø·Ù„Ø§ÛŒÛŒ Ùˆ Ù†Ø§Ø±Ù†Ø¬ÛŒ ØªØ§Ø¨Ø§Ù†" else "Warm solar pearl background with radiant incandescent amber accents"
                        ),
                        Triple(
                            "vip_quantum_lilac",
                            if (isPersian) "Quantum Vaporwave (ÛŒØ§Ø³ÛŒ Ù¾Ø§Ø³ØªÙ„ÛŒ / Ø¨Ù†ÙØ´)" else "Quantum Vaporwave (Lilac & Electric Violet)",
                            if (isPersian) "ØªÙ… Ø¨Ø³ÛŒØ§Ø± Ø´ÛŒÚ© Ø¨Ø§ ØªÙ†â€ŒÙ‡Ø§ÛŒ ÛŒØ§Ø³ÛŒ Ø±ÙˆØ´Ù† Ùˆ Ø¨Ù†ÙØ´ Ø³Ø§ÛŒØ¨Ø±ÛŒ Ø¢ÛŒÙ†Ø¯Ù‡â€ŒÙ†Ú¯Ø±Ø§Ù†Ù‡" else "Pastel lilac aesthetic with electric neon violet lines and readable dark text"
                        ),
                        Triple(
                            "vip_arctic_ice",
                            if (isPersian) "Arctic Glacial Ice (ÛŒØ®ÛŒ Ù‚Ø·Ø¨ÛŒ / ÙÛŒØ±ÙˆØ²Ù‡â€ŒØ§ÛŒ)" else "Arctic Glacial Ice (Glacial Frost & Vivid Cyan)",
                            if (isPersian) "Ø³ÙÛŒØ¯ ÛŒØ®ÛŒ Ù‚Ø·Ø¨ÛŒ Ø¨Ø§ Ø®Ø·ÙˆØ· ÙÛŒØ±ÙˆØ²Ù‡â€ŒØ§ÛŒ Ùˆ Ù„Ø§Ø¬ÙˆØ±Ø¯ÛŒ Ø´ÙØ§Ù Ùˆ Ú†Ø´Ù…â€ŒÙ†ÙˆØ§Ø²" else "Ultra-clean glacial ice white with electric cyan borders and deep navy clarity"
                        )
                    )

                    lightVipThemes.forEach { (id, title, desc) ->
                        val selected = themeMode == id
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ),
                            border = BorderStroke(
                                width = if (selected) 1.5.dp else 0.8.dp,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { vm.setTheme(id) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selected,
                                    onClick = { vm.setTheme(id) }
                                )
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Group 2: 5 Masterpiece Dark / Hacker Themes
                    Text(
                        text = if (isPersian) "ðŸ•¶ï¸ Ûµ ØªÙ… Ø§Ø®ØªØµØ§ØµÛŒ ØªØ§Ø±ÛŒÚ© Ùˆ Ù‡Ú©Ø±ÛŒ (Hacker VIP)" else "ðŸ•¶ï¸ 5 Masterpiece Dark / Hacker VIP Themes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    val darkVipThemes = listOf(
                        Triple(
                            "vip_phantom_hacker",
                            if (isPersian) "Phantom Hacker (Ù…Ø´Ú©ÛŒ OLED / Ù†Ø¹Ù†Ø§ÛŒÛŒ Ù†Ø¦ÙˆÙ†)" else "Phantom Hacker (Void Black & Radioactive Mint)",
                            if (isPersian) "Ø³ÛŒØ§Ù‡ÛŒ Ù…Ø·Ù„Ù‚ Ù†Ù…Ø§ÛŒØ´Ú¯Ø±Ù‡Ø§ÛŒ Ø§ÙˆÙ„Ø¯ Ø¨Ø§ ØªØ§Ø¨Ø´ ÙØ³ÙØ±ÛŒ Ø³Ø¨Ø² Ù†Ø¹Ù†Ø§ÛŒÛŒ Ø±Ø§Ø¯ÛŒÙˆØ§Ú©ØªÛŒÙˆ" else "Pitch void black OLED canvas ignited by radioactive toxic mint lasers"
                        ),
                        Triple(
                            "vip_blood_overdrive",
                            if (isPersian) "Blood Overdrive (Ø¢Ø¨Ø³ÛŒØ¯ÛŒÙ† / Ù‚Ø±Ù…Ø² Ø³Ø§ÛŒØ¨Ø±Ù¾Ø§Ù†Ú©)" else "Blood Overdrive (Obsidian Black & Neon Blood Red)",
                            if (isPersian) "Ø¸Ù„Ù…Øª Ø³Ø§ÛŒØ¨Ø±ÛŒ Ø¨Ø§ Ø®Ø·ÙˆØ· Ù†Ø¦ÙˆÙ† Ù‚Ø±Ù…Ø² Ø®ÙˆÙ†ÛŒ Ù¾Ø±Ø§Ù†Ø±Ú˜ÛŒ Ùˆ Ù¾Ø±Ú©Ù†ØªØ±Ø§Ø³Øª" else "Total obsidian blackness electrified by hyper-vivid crimson neon glows"
                        ),
                        Triple(
                            "vip_synthwave_2077",
                            if (isPersian) "Synthwave 2077 (Ù†Ø¦Ùˆ ØªÙˆÚ©ÛŒÙˆ ØµÙˆØ±ØªÛŒ Ùˆ Ø³Ø§ÛŒØ§Ù†)" else "Synthwave 2077 (Neo Tokyo Laser Pink & Cyan)",
                            if (isPersian) "Ù‡Ø§Ø±Ù…ÙˆÙ†ÛŒ Ø¯ÙˆØªØ§ÛŒÛŒ ØµÙˆØ±ØªÛŒ Ù†Ø¦ÙˆÙ†ÛŒ Ùˆ Ø¢Ø¨ÛŒ Ø³Ø§ÛŒØ§Ù† Ø¯Ø± ÙØ¶Ø§ÛŒ Ø³Ø§ÛŒØ¨Ø±Ù¾Ø§Ù†Ú© Û²Û°Û·Û·" else "Dual laser pink and cyan retro-futurism set in deep midnight Tokyo"
                        ),
                        Triple(
                            "vip_titanium_monolith",
                            if (isPersian) "Titanium Monolith (Ú¯Ø±Ø§ÙÛŒØª ØªØ§Ú©ØªÛŒÚ©Ø§Ù„ ØªÛŒØ±Ù‡)" else "Titanium Monolith (Tactical Graphite & Titanium)",
                            if (isPersian) "Ø·Ø±Ø§Ø­ÛŒ Ù†Ø¸Ø§Ù…ÛŒ Ù…ÛŒÙ†ÛŒÙ…Ø§Ù„ Ø§Ø² Ú¯Ø±Ø§ÙÛŒØª Ù…Ø§ØªØŒ ÙÙˆÙ„Ø§Ø¯ Ø®Ø§Ú©Ø³ØªØ±ÛŒ Ùˆ Ù†Ù‚Ø±Ù‡â€ŒØ§ÛŒ Ù…ØªØ§Ù„ÛŒÚ©" else "Stealth tactical graphite structure crafted with brushed cold titanium"
                        ),
                        Triple(
                            "vip_matrix_terminal_zero",
                            if (isPersian) "Matrix Terminal Zero (ÙØ³ÙØ±ÛŒ ØªØ±Ù…ÛŒÙ†Ø§Ù„ CRT)" else "Matrix Terminal Zero (CRT Phosphor Green)",
                            if (isPersian) "Ù†Ù…Ø§ÛŒØ´Ú¯Ø± Ø¨Ø§Ø³ØªØ§Ù†ÛŒ Ù‡Ú©Ø±ÛŒ Ø³Ø¨Ø² ÙØ³ÙØ±ÛŒ Û±Û¹Û¸Û´ Ø¨Ø± Ø±ÙˆÛŒ Ø²Ù…ÛŒÙ†Ù‡ ØªØ§Ø±ÛŒÚ© Ø®Ø§Ù„Øµ" else "Retro 1980s mainframe CRT phosphor green scanning pure deep space"
                        )
                    )

                    darkVipThemes.forEach { (id, title, desc) ->
                        val selected = themeMode == id
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ),
                            border = BorderStroke(
                                width = if (selected) 1.5.dp else 0.8.dp,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { vm.setTheme(id) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selected,
                                    onClick = { vm.setTheme(id) }
                                )
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { vipThemesDialog = false }) {
                    Text(stringResource(R.string.common_ok))
                }
            }
        )
    }


    if (cellStyleDialog) {
        AlertDialog(
            onDismissRequest = { cellStyleDialog = false },
            title = { Text(stringResource(R.string.settings_cell_style_title)) },
            text = {
                Column {
                    val options = listOf(
                        "card" to stringResource(R.string.cell_style_card),
                        "divider" to stringResource(R.string.cell_style_divider)
                    )
                    options.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.cellStyle = code
                                    cellStyleDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.cellStyle == code,
                                onClick = {
                                    vm.settings.cellStyle = code
                                    cellStyleDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { cellStyleDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (cellOpacityDialog) {
        AlertDialog(
            onDismissRequest = { cellOpacityDialog = false },
            title = { Text(stringResource(R.string.settings_border_opacity_title)) },
            text = {
                Column {
                    val options = listOf(
                        "subtle" to stringResource(R.string.border_opacity_subtle),
                        "medium" to stringResource(R.string.border_opacity_medium),
                        "strong" to stringResource(R.string.border_opacity_strong),
                        "neon" to stringResource(R.string.border_opacity_neon)
                    )
                    options.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.cellBorderOpacity = code
                                    cellOpacityDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.cellBorderOpacity == code,
                                onClick = {
                                    vm.settings.cellBorderOpacity = code
                                    cellOpacityDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { cellOpacityDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (dividerColorDialog) {
        AlertDialog(
            onDismissRequest = { dividerColorDialog = false },
            title = { Text(stringResource(R.string.settings_divider_color_title)) },
            text = {
                Column {
                    val options = listOf(
                        "theme" to "هماهنگ با تم انتخابی (پیش‌فرض)",
                        "gold" to "طلایی استارک (Stark Gold)",
                        "green" to "سبز ماتریکس (Matrix Green)",
                        "cyan" to "آبی سایان (Cyber Cyan)",
                        "silver" to "نقره‌ای شبح (Ghost Silver)",
                        "subtle" to "خاکستری مات (Subtle Dim)"
                    )
                    options.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.dividerColor = code
                                    dividerColorDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.dividerColor == code,
                                onClick = {
                                    vm.settings.dividerColor = code
                                    dividerColorDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { dividerColorDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (borderOpacitySliderDialog) {
        var tempOpacity by remember { mutableStateOf(vm.settings.cellBorderOpacityFloat) }
        AlertDialog(
            onDismissRequest = { borderOpacitySliderDialog = false },
            title = { Text(stringResource(R.string.settings_border_opacity_slider_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${(tempOpacity * 100).toInt()}%",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Slider(
                        value = tempOpacity,
                        onValueChange = { tempOpacity = it },
                        valueRange = 0.10f..1.0f
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("10%", style = MaterialTheme.typography.labelSmall)
                        Text("100%", style = MaterialTheme.typography.labelSmall)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.settings.cellBorderOpacityFloat = tempOpacity
                    borderOpacitySliderDialog = false
                }) { Text(stringResource(R.string.common_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { borderOpacitySliderDialog = false }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (borderWidthSliderDialog) {
        var tempWidth by remember { mutableStateOf(vm.settings.cellBorderWidthFloat) }
        AlertDialog(
            onDismissRequest = { borderWidthSliderDialog = false },
            title = { Text(stringResource(R.string.settings_border_width_slider_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = String.format(java.util.Locale.US, "%.1f dp", tempWidth),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Slider(
                        value = tempWidth,
                        onValueChange = { tempWidth = it },
                        valueRange = 0.4f..4.0f
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0.4 dp", style = MaterialTheme.typography.labelSmall)
                        Text("4.0 dp", style = MaterialTheme.typography.labelSmall)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.settings.cellBorderWidthFloat = tempWidth
                    borderWidthSliderDialog = false
                }) { Text(stringResource(R.string.common_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { borderWidthSliderDialog = false }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (messageCardShapeDialog) {
        AlertDialog(
            onDismissRequest = { messageCardShapeDialog = false },
            title = { Text(stringResource(R.string.settings_message_card_shape_title)) },
            text = {
                Column {
                    val shapeOptions = listOf(
                        SettingsStore.SHAPE_ROUNDED to "Rounded (12dp)",
                        SettingsStore.SHAPE_CYBER to "Cyberpunk (4dp)",
                        SettingsStore.SHAPE_BUBBLE to "Classic Bubble",
                        SettingsStore.SHAPE_MINIMAL to "Minimal (2dp)",
                        SettingsStore.SHAPE_CAPSULE to "Capsule (22dp)"
                    )
                    shapeOptions.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.messageCardShape = code
                                    messageCardShapeDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.messageCardShape == code,
                                onClick = {
                                    vm.settings.messageCardShape = code
                                    messageCardShapeDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { messageCardShapeDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }
    if (launcherIconDialog) {
        AlertDialog(
            onDismissRequest = { launcherIconDialog = false },
            title = { Text(stringResource(R.string.settings_launcher_icon_title)) },
            text = {
                Column {
                    val options = listOf(
                        SettingsStore.ICON_BLACK_WHITE to stringResource(R.string.launcher_icon_black_white),
                        SettingsStore.ICON_STARK_GOLD to stringResource(R.string.launcher_icon_stark_gold),
                        SettingsStore.ICON_MATRIX_GREEN to stringResource(R.string.launcher_icon_matrix_green),
                        SettingsStore.ICON_CYBER_CYAN to stringResource(R.string.launcher_icon_cyber_cyan)
                    )
                    options.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.launcherIcon = code
                                    launcherIconDialog = false
                                    Toast.makeText(context, "استایل آیکون تنظیم شد", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.launcherIcon == code,
                                onClick = {
                                    vm.settings.launcherIcon = code
                                    launcherIconDialog = false
                                    Toast.makeText(context, "استایل آیکون تنظیم شد", Toast.LENGTH_SHORT).show()
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { launcherIconDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (delaySecondsDialog) {
        AlertDialog(
            onDismissRequest = { delaySecondsDialog = false },
            title = { Text(stringResource(R.string.settings_delay_secs_title)) },
            text = {
                Column {
                    val options = listOf(1, 2, 3, 5, 10, 15, 30)
                    options.forEach { sec ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.delaySeconds = sec
                                    delaySecondsDialog = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = delaySeconds == sec,
                                onClick = {
                                    vm.settings.delaySeconds = sec
                                    delaySecondsDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(String.format(stringResource(R.string.settings_delay_secs_format), sec))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { delaySecondsDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (swipeLeftDialog || swipeRightDialog) {
        val isLeft = swipeLeftDialog
        val currentAction = if (isLeft) vm.settings.swipeLeftAction else vm.settings.swipeRightAction
        val actions = listOf(
            SwipeAction.ARCHIVE to stringResource(R.string.access_archive),
            SwipeAction.DELETE to stringResource(R.string.sheet_delete),
            SwipeAction.MARK_READ_UNREAD to stringResource(R.string.swipe_action_mark_read),
            SwipeAction.PIN to stringResource(R.string.swipe_action_pin),
            SwipeAction.BLOCK to stringResource(R.string.swipe_action_block),
            SwipeAction.OFF to stringResource(R.string.swipe_action_off)
        )
        AlertDialog(
            onDismissRequest = {
                swipeLeftDialog = false
                swipeRightDialog = false
            },
            title = { Text(stringResource(if (isLeft) R.string.swipe_left_title else R.string.swipe_right_title)) },
            text = {
                Column {
                    actions.forEach { (action, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isLeft) vm.settings.swipeLeftAction = action
                                    else vm.settings.swipeRightAction = action
                                    swipeLeftDialog = false
                                    swipeRightDialog = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = currentAction == action,
                                onClick = {
                                    if (isLeft) vm.settings.swipeLeftAction = action
                                    else vm.settings.swipeRightAction = action
                                    swipeLeftDialog = false
                                    swipeRightDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    swipeLeftDialog = false
                    swipeRightDialog = false
                }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (languageDialog) {
        AlertDialog(
            onDismissRequest = { languageDialog = false },
            title = { Text(stringResource(R.string.settings_language_title)) },
            text = {
                Column {
                    val langOptions = listOf(
                        "system" to stringResource(R.string.language_system),
                        "fa" to stringResource(R.string.language_fa),
                        "en" to stringResource(R.string.language_en)
                    )
                    langOptions.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.appLanguage = code
                                    languageDialog = false
                                    (context as? Activity)?.recreate()
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.appLanguage == code,
                                onClick = {
                                    vm.settings.appLanguage = code
                                    languageDialog = false
                                    (context as? Activity)?.recreate()
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { languageDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (calendarDialog) {
        AlertDialog(
            onDismissRequest = { calendarDialog = false },
            title = { Text("نوع تقویم پیام‌ها") },
            text = {
                Column {
                    val calOptions = listOf(
                        SettingsStore.CALENDAR_JALALI to "شمسی (جلالی) - پیش‌فرض",
                        SettingsStore.CALENDAR_GREGORIAN to "میلادی (گرگوریان)"
                    )
                    calOptions.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.calendarType = code
                                    calendarDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.calendarType == code,
                                onClick = {
                                    vm.settings.calendarType = code
                                    calendarDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { calendarDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (previewLinesDialog) {
        AlertDialog(
            onDismissRequest = { previewLinesDialog = false },
            title = { Text(stringResource(R.string.settings_preview_lines_title)) },
            text = {
                Column {
                    val lineOptions = listOf(
                        1 to stringResource(R.string.preview_lines_1),
                        2 to stringResource(R.string.preview_lines_2),
                        3 to stringResource(R.string.preview_lines_3)
                    )
                    lineOptions.forEach { (lines, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.previewLines = lines
                                    previewLinesDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.previewLines == lines,
                                onClick = {
                                    vm.settings.previewLines = lines
                                    previewLinesDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { previewLinesDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (cellColorDialog) {
        AlertDialog(
            onDismissRequest = { cellColorDialog = false },
            title = { Text(stringResource(R.string.settings_cell_color_title)) },
            text = {
                Column {
                    val colorOptions = listOf(
                        "theme" to stringResource(R.string.cell_color_theme),
                        "green" to stringResource(R.string.cell_color_green),
                        "cyan" to stringResource(R.string.cell_color_cyan),
                        "gold" to stringResource(R.string.cell_color_gold),
                        "red" to stringResource(R.string.cell_color_red),
                        "purple" to stringResource(R.string.cell_color_purple),
                        "silver" to stringResource(R.string.cell_color_silver)
                    )
                    colorOptions.forEach { (code, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    vm.settings.customCellColor = code
                                    cellColorDialog = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = vm.settings.customCellColor == code,
                                onClick = {
                                    vm.settings.customCellColor = code
                                    cellColorDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { cellColorDialog = false }) { Text(stringResource(R.string.common_ok)) }
            }
        )
    }

    if (soundDialog) {
        AlertDialog(
            onDismissRequest = { soundDialog = false },
            title = { Text(stringResource(R.string.settings_pin_notification_sound)) },
            text = {
                Column {
                    Text(
                        stringResource(R.string.settings_sound_arrival),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    notificationSoundOptions.forEach { (value, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    notificationSound = value
                                    NotificationHelper.previewNotificationSound(context, value)
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = notificationSound == value,
                                onClick = {
                                    notificationSound = value
                                    NotificationHelper.previewNotificationSound(context, value)
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.settings.notificationSound = notificationSound
                    NotificationHelper.ensureChannel(context)
                    soundDialog = false
                }) { Text(stringResource(R.string.common_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { soundDialog = false }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (simDialog) {
        var selected by remember { mutableIntStateOf(vm.settings.simSubscriptionId) }
        val options = mutableListOf(-1 to context.getString(R.string.settings_sim_default))
        sims.forEach { sub ->
            val carrier = sub.carrierName?.ifBlank { null }
            val label = if (carrier != null) String.format(context.getString(R.string.settings_sim_label_format), carrier, sub.slotIndex + 1) else String.format(context.getString(R.string.settings_sim_label), sub.slotIndex + 1)
            options += sub.subscriptionId to label
        }
        AlertDialog(
            onDismissRequest = { simDialog = false },
            title = { Text(stringResource(R.string.settings_pin_sim_card)) },
            text = {
                Column {
                    options.forEach { (id, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selected = id }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selected == id,
                                onClick = { selected = id }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.settings.simSubscriptionId = selected
                    simDialog = false
                }) { Text(stringResource(R.string.common_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { simDialog = false }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (importSourceDialog) {
        var source by remember { mutableStateOf(ImportSource.OWN_BACKUP) }
        fun launchPicker() {
            importSourceDialog = false
            if (source == ImportSource.OWN_BACKUP) {
                importLauncher.launch(
                    arrayOf("application/octet-stream", "application/x-sqlite3")
                )
            } else {
                smsIeLauncher.launch(
                    arrayOf(
                        "application/zip",
                        "application/json",
                        "application/octet-stream",
                        "*/*"
                    )
                )
            }
        }
        AlertDialog(
            onDismissRequest = { importSourceDialog = false },
            title = { Text(stringResource(R.string.settings_import_source_title)) },
            text = {
                ImportRadioGroup(
                    options = listOf(
                        stringResource(R.string.settings_import_source_own_title) to "",
                        stringResource(R.string.settings_import_source_sms_ie_title) to ""
                    ),
                    selectedIndex = if (source == ImportSource.OWN_BACKUP) 0 else 1,
                    onSelect = {
                        source = if (it == 0) ImportSource.OWN_BACKUP else ImportSource.SMS_IE
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { launchPicker() }) {
                    Text(stringResource(R.string.common_continue))
                }
            },
            dismissButton = {
                TextButton(onClick = { importSourceDialog = false }) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    if (importModeDialog) {
        val uri = pendingImportUri
        var mode by remember {
            mutableStateOf(com.anindra.messages.data.ImportMode.MERGE)
        }
        fun applyImport() {
            val target = uri ?: return
            if (pendingImportSource == ImportSource.SMS_IE) {
                vm.importSmsIe(target, mode, showImportResult)
                return
            }
            when {
                pendingImportFormat == com.anindra.messages.data.BackupFormat.PIN -> {
                    pinInput = ""
                    pinError = null
                    pinMode = PinDialogMode.ENTER
                }
                else -> vm.importDatabase(target, null, mode, showImportResult)
            }
        }
        AlertDialog(
            onDismissRequest = {
                importModeDialog = false
                pendingImportUri = null
            },
            title = { Text(stringResource(R.string.settings_import_backup)) },
            text = {
                Column {
                    Text(
                        stringResource(R.string.settings_backup_apply),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ImportRadioGroup(
                        options = listOf(
                            stringResource(R.string.settings_merge_title) to
                                stringResource(R.string.settings_merge_subtitle),
                            stringResource(R.string.settings_restore_title) to
                                stringResource(R.string.settings_restore_subtitle)
                        ),
                        selectedIndex = if (mode == com.anindra.messages.data.ImportMode.MERGE) 0 else 1,
                        onSelect = {
                            mode = if (it == 0) {
                                com.anindra.messages.data.ImportMode.MERGE
                            } else {
                                com.anindra.messages.data.ImportMode.REPLACE
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    importModeDialog = false
                    pendingImportMode = mode
                    applyImport()
                }) { Text(stringResource(R.string.settings_import)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    importModeDialog = false
                    pendingImportUri = null
                }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    fun reportExport(result: com.anindra.messages.data.Repository.ExportResult, encrypted: Boolean = true) {
        val location = if (BackupLocation.isCustom(backupFolder)) BackupLocation.label(backupFolder)
        else context.getString(R.string.settings_backup_location_default)
        val message = when (result) {
            is com.anindra.messages.data.Repository.ExportResult.Success ->
                String.format(
                    context.getString(
                        if (encrypted) R.string.settings_backup_saved_location
                        else R.string.settings_backup_saved_location_unencrypted
                    ),
                    location
                )
            is com.anindra.messages.data.Repository.ExportResult.Error ->
                String.format(context.getString(R.string.settings_import_failed), result.message)
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    pinMode?.let { mode ->
        AlertDialog(
            onDismissRequest = {
                pinMode = null
                pendingImportUri = null
            },
            title = {
                Text(
                    if (mode == PinDialogMode.SET) stringResource(R.string.settings_pin_dialog_set)
                    else stringResource(R.string.settings_pin_dialog_enter)
                )
            },
            text = {
                Column {
                    if (mode == PinDialogMode.SET) {
                        Text(
                            stringResource(R.string.settings_backup_pin_protection),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    backupFolderLauncher.launch(
                                        if (backupFolder.isBlank()) null else Uri.parse(backupFolder)
                                    )
                                }
                                .padding(vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Folder,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    stringResource(R.string.settings_backup_save_to),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    if (BackupLocation.isCustom(backupFolder)) BackupLocation.label(backupFolder)
                                    else context.getString(R.string.settings_backup_location_default_option)
                                )
                            }
                            Text(
                                stringResource(R.string.settings_backup_change),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { new ->
                            if (new.length <= 16 && new.all { it.isDigit() }) pinInput = new
                        },
                        label = { Text(stringResource(R.string.settings_pin_enter)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (mode == PinDialogMode.SET) {
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = pinConfirm,
                            onValueChange = { new ->
                                if (new.length <= 16 && new.all { it.isDigit() }) pinConfirm = new
                            },
                            label = { Text(stringResource(R.string.settings_pin_repeat)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (pinError != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = pinError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (mode == PinDialogMode.SET) {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val enabled = !periodicBackup
                                    periodicBackup = enabled
                                    vm.settings.periodicBackupEnabled = enabled
                                    PeriodicBackupScheduler.apply(context, enabled, vm.settings.periodicBackupInterval)
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                stringResource(R.string.settings_periodic_backup_title),
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = periodicBackup,
                                onCheckedChange = { enabled ->
                                    periodicBackup = enabled
                                    vm.settings.periodicBackupEnabled = enabled
                                    PeriodicBackupScheduler.apply(context, enabled, vm.settings.periodicBackupInterval)
                                }
                            )
                        }
                        if (periodicBackup) {
                            Text(
                                stringResource(R.string.settings_periodic_backup_interval_title),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            listOf(
                                PeriodicBackupScheduler.INTERVAL_DAILY to stringResource(R.string.settings_periodic_backup_daily),
                                PeriodicBackupScheduler.INTERVAL_WEEKLY to stringResource(R.string.settings_periodic_backup_weekly)
                            ).forEach { (value, label) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            vm.settings.periodicBackupInterval = value
                                            PeriodicBackupScheduler.apply(context, true, value)
                                        }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = vm.settings.periodicBackupInterval == value,
                                        onClick = {
                                            vm.settings.periodicBackupInterval = value
                                            PeriodicBackupScheduler.apply(context, true, value)
                                        }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(label)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val pin = pinInput.trim()
                    when {
                        mode == PinDialogMode.SET && pin.isEmpty() -> {
                            pinMode = null
                            showRawBackupWarning = true
                        }
                        pin.length < 4 -> pinError = context.getString(R.string.settings_pin_error_too_short)
                        mode == PinDialogMode.SET && pin != pinConfirm ->
                            pinError = context.getString(R.string.settings_pin_error_mismatch)
                        else -> {
                            if (mode == PinDialogMode.SET) {
                                pinMode = null
                                vm.backupDatabase(pin) { reportExport(it) }
                            } else {
                                val uri = pendingImportUri
                                if (uri != null) {
                                    pinMode = null
                                    pendingImportUri = null
                                    vm.importDatabase(uri, pin, pendingImportMode, showImportResult)
                                }
                            }
                        }
                    }
                }) { Text(if (mode == PinDialogMode.SET) stringResource(R.string.settings_save) else stringResource(R.string.settings_import)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    pinMode = null
                    pendingImportUri = null
                }) { Text(stringResource(R.string.common_cancel)) }
            }
        )
    }

    if (showRawBackupWarning) {
        AlertDialog(
            onDismissRequest = { showRawBackupWarning = false },
            title = { Text(stringResource(R.string.settings_backup_unencrypted_title)) },
            text = { Text(stringResource(R.string.settings_backup_unencrypted_warning)) },
            confirmButton = {
                TextButton(onClick = {
                    showRawBackupWarning = false
                    vm.backupDatabaseUnencrypted { reportExport(it, encrypted = false) }
                }) { Text(stringResource(R.string.settings_backup_unencrypted_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showRawBackupWarning = false }) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    val importLoading = vm.importLoading.value
    if (importLoading != null) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(stringResource(R.string.settings_loading)) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text(context.getString(R.string.settings_loading_progress, importLoading), style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)
    )
}

private fun swipeActionLabel(action: SwipeAction, context: android.content.Context): String = when (action) {
    SwipeAction.ARCHIVE -> context.getString(R.string.access_archive)
    SwipeAction.DELETE -> context.getString(R.string.sheet_delete)
    SwipeAction.MARK_READ_UNREAD -> context.getString(R.string.swipe_action_mark_read)
    SwipeAction.PIN -> context.getString(R.string.swipe_action_pin)
    SwipeAction.BLOCK -> context.getString(R.string.swipe_action_block)
    SwipeAction.OFF -> context.getString(R.string.swipe_action_off)
}

@Composable
fun GroupedRowCard(
    position: RowPosition,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val corners = rowCorners(position)
    Card(
        shape = RoundedCornerShape(
            topStart = corners.topStart,
            topEnd = corners.topEnd,
            bottomEnd = corners.bottomEnd,
            bottomStart = corners.bottomStart
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SettingsGroup(content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(SettingsLayout.ROW_GAP)) {
        content()
    }
}

@Composable
fun GroupedRowList(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(SettingsLayout.OUTER_RADIUS),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column { content() }
    }
}

private fun openSystemNotificationSettings(context: android.content.Context) {
    runCatching {
        context.startActivity(
            android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

private fun themeLabel(mode: String, context: android.content.Context): String {
    val isFa = java.util.Locale.getDefault().language == "fa"
    return when (mode) {
        "stark", "stark_hud" -> "Stark Cyber HUD"
        "cyber", "cyber_blue", "ab_cyber_blue" -> "AB Cyber Blue"
        "blackout" -> "Blackout (OLED)"
        "matrix", "neon_matrix" -> "Neon Matrix"
        "arctic", "arctic_glass" -> "Arctic Glass"
        "ghost", "ghost_protocol" -> "Ghost Protocol"
        "ocean", "deep_ocean" -> "Deep Ocean"
        "crimson", "crimson_hacker" -> "Crimson Hacker"
        "quantum" -> "Quantum"
        "orange", "cyber_orange" -> "Cyber Orange"
        "minimal", "minimal_glass" -> "Minimal Glass"
        "light" -> context.getString(R.string.settings_theme_light)
        "dark" -> context.getString(R.string.settings_theme_dark)
        "amoled" -> context.getString(R.string.settings_theme_amoled)
        "dynamic" -> context.getString(R.string.settings_theme_dynamic)
        // VIP Light Themes
        "vip_stark_clean" -> if (isFa) "VIP: Ø§Ø³ØªØ§Ø±Ú© Ø³ÙÛŒØ¯ Ù…Ø¯Ø±Ù† (Stark Clean)" else "VIP: Stark Clean Tech (White & Cobalt)"
        "vip_matrix_light" -> if (isFa) "VIP: Ù…Ø§ØªØ±ÛŒÚ©Ø³ Ø±ÙˆØ´Ù† (Matrix Light)" else "VIP: Neo Matrix Light (Digital Paper & Emerald)"
        "vip_sunset_amber" -> if (isFa) "VIP: ØºØ±ÙˆØ¨ Ú©Ù‡Ø±Ø¨Ø§ÛŒÛŒ (Sunset Amber)" else "VIP: Solar Amber Sunset (Warm Solar & Amber)"
        "vip_quantum_lilac" -> if (isFa) "VIP: Ú©ÙˆØ§Ù†ØªÙˆÙ… ÛŒØ§Ø³ÛŒ (Quantum Lilac)" else "VIP: Quantum Vaporwave (Lilac & Violet)"
        "vip_arctic_ice" -> if (isFa) "VIP: ÛŒØ® Ù‚Ø·Ø¨ÛŒ Ø¢Ø±Ú©ØªÛŒÚ© (Arctic Ice)" else "VIP: Arctic Glacial Ice (Glacial Frost & Vivid Cyan)"
        // VIP Dark Themes
        "vip_phantom_hacker" -> if (isFa) "VIP: ÙØ§Ù†ØªÙˆÙ… Ù‡Ú©Ø± (Phantom Hacker)" else "VIP: Phantom Hacker (Void Black & Radioactive Mint)"
        "vip_blood_overdrive" -> if (isFa) "VIP: Ø§ÙˆØ±Ø¯Ø±Ø§ÛŒÙˆ Ø®ÙˆÙ†ÛŒÙ† (Blood Overdrive)" else "VIP: Blood Overdrive (Obsidian Black & Neon Blood Red)"
        "vip_synthwave_2077" -> if (isFa) "VIP: Ø³ÛŒÙ†Øªâ€ŒÙˆÛŒÙˆ Û²Û°Û·Û· (Synthwave 2077)" else "VIP: Synthwave 2077 (Neo Tokyo Laser Pink & Cyan)"
        "vip_titanium_monolith" -> if (isFa) "VIP: ØªÛŒØªØ§Ù†ÛŒÙˆÙ… Ù…Ù†ÙˆÙ„ÛŒØª (Titanium Monolith)" else "VIP: Titanium Monolith (Graphite & Titanium)"
        "vip_matrix_terminal_zero" -> if (isFa) "VIP: ØªØ±Ù…ÛŒÙ†Ø§Ù„ Ù…Ø§ØªØ±ÛŒÚ©Ø³ Ø²ÛŒØ±Ùˆ (Terminal Zero)" else "VIP: Matrix Terminal Zero (CRT Phosphor Green)"
        else -> "Stark Cyber HUD"
    }
}

private fun notificationSoundLabel(value: String, options: List<Pair<String, String>>, context: android.content.Context) =
    options.firstOrNull { it.first == value }?.second ?: context.getString(R.string.settings_sound_default)

@Composable
internal fun ImportRadioGroup(
    options: List<Pair<String, String>>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        options.forEachIndexed { index, (label, _) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .selectable(
                        selected = selectedIndex == index,
                        onClick = { onSelect(index) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                RadioButton(selected = selectedIndex == index, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(label, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean? = null,
    onChecked: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    position: RowPosition = RowPosition.SINGLE,
    trailing: (@Composable () -> Unit)? = null,
    preview: (@Composable () -> Unit)? = null
) {
    val contentAlpha = if (enabled) 1f else 0.38f
    val corners = rowCorners(position)
    Card(
        shape = RoundedCornerShape(
            topStart = corners.topStart,
            topEnd = corners.topEnd,
            bottomEnd = corners.bottomEnd,
            bottomStart = corners.bottomStart
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled && (onClick != null || checked != null)) {
                    if (checked != null && onChecked != null) onChecked(!checked) else onClick?.invoke()
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = SettingsLayout.rowMinHeight(
                            hasSubtitle = subtitle != null,
                            largeTouchTargets = LocalLargeTouchTargets.current
                        )
                    )
                    .padding(
                        horizontal = SettingsLayout.ROW_CONTENT_PADDING,
                        vertical = 12.dp
                    )
            ) {
                Column(Modifier.weight(1f).alpha(contentAlpha)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (subtitle != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (checked != null && onChecked != null) {
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = checked,
                        onCheckedChange = { onChecked(it) },
                        enabled = enabled,
                        modifier = Modifier.size(width = 52.dp, height = 32.dp)
                    )
                } else if (trailing != null) {
                    trailing()
                }
            }
            if (preview != null) {
                Box(
                    Modifier.padding(
                        start = SettingsLayout.PREVIEW_INSET,
                        end = SettingsLayout.PREVIEW_INSET,
                        bottom = SettingsLayout.PREVIEW_INSET
                    )
                ) { preview() }
            }
        }
    }
}

@Composable
fun SettingsFooter(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SettingsLayout.FOOTER_TOP_GAP,
                bottom = SettingsLayout.FOOTER_BOTTOM_GAP
            )
    )
}
