package com.anindra.messages.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Google-Messages-like customization store. */
class SettingsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("messages_settings", Context.MODE_PRIVATE)

    private val _revision = MutableStateFlow(0)
    val revision: StateFlow<Int> = _revision.asStateFlow()

    companion object {
        const val THEME_STARK = "stark"
        const val THEME_ORANGE = "orange"
        const val KEY_VIP_THEMES_UNLOCKED = "vip_themes_unlocked"
        const val VIP_TOKEN_1 = "23882323"
        const val VIP_TOKEN_2 = "238823"
        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_AMOLED = "amoled"
        const val THEME_DYNAMIC = "dynamic"

        const val KEY_THEME = "theme_mode"
        const val KEY_NOTIFICATIONS = "notifications_enabled"
        const val KEY_SOUNDS = "message_sounds_enabled"
        const val KEY_DELIVERY_REPORTS = "delivery_reports_enabled"
        const val KEY_SIM_SUBSCRIPTION_ID = "sim_subscription_id"
        const val KEY_PINNED_ENABLED = "pinned_enabled"
        const val KEY_ARCHIVING_ENABLED = "archiving_enabled"
        const val KEY_SWIPE_ACTIONS_ENABLED = "swipe_actions_enabled"
        const val KEY_BLOCKING_ENABLED = "blocking_enabled"
        const val KEY_RETENTION_TRASH_DAYS = "retention_trash_days"
        const val KEY_RETENTION_SPAM_DAYS = "retention_spam_days"
        const val KEY_RETENTION_ENABLED = "retention_enabled"
        const val KEY_RETENTION_TRASH = "retention_trash"
        const val KEY_RETENTION_KEYWORD = "retention_keyword_messages"
        const val KEY_RETENTION_BLOCKED = "retention_blocked_senders"
        const val KEY_FORWARDING_ENABLED = "forwarding_enabled"
        const val KEY_UNREAD_AT_TOP_ENABLED = "unread_at_top_enabled"
        const val KEY_SCHEDULED_MESSAGES_ENABLED = "scheduled_messages_enabled"
        const val KEY_DELAYED_SENDING_ENABLED = "delayed_sending_enabled"
        const val KEY_DELAY_SECONDS = "delay_seconds"
        const val KEY_HIGHLIGHT_LINKS = "highlight_links"
        const val KEY_HIDE_LINKS = "hide_links"
        const val KEY_PRIVACY_MODE = "privacy_mode"
        const val KEY_APP_LOCK = "app_lock_enabled"
        const val KEY_FIRST_IMPORT_DONE = "first_import_done"
        const val KEY_PARTICIPANTS_MIGRATED = "participants_migrated"
        const val KEY_ALPHANUMERIC_REPAIR_DONE = "alphanumeric_repair_done"
        const val KEY_PHONE_REGION = "phone_region"
        const val KEY_SEND_SOUND = "send_sound_enabled"
        const val KEY_RECEIVE_SOUND = "receive_sound_enabled"
        const val KEY_NOTIFICATION_SOUND = "notification_sound"
        const val KEY_NOTIF_ACTION_REPLY = "notif_action_reply"
        const val KEY_NOTIF_ACTION_MARK_READ = "notif_action_mark_read"
        const val KEY_NOTIF_ACTION_DELETE = "notif_action_delete"
        const val NOTIFY_SOUND_DEFAULT = "default"
        const val NOTIFY_SOUND_APP = "app_sound"
        const val NOTIFY_SOUND_DRAGON = "dragon_studio"
        const val NOTIFY_SOUND_UNIVERSFIELD_09 = "universfield_09"
        const val NOTIFY_SOUND_UNIVERSFIELD_062 = "universfield_062"
        const val KEY_SHOW_SIM_INDICATOR = "show_sim_indicator"
        const val KEY_EMOJI_BUTTON = "emoji_button_enabled"
        const val KEY_PERMANENT_DELETE = "permanent_delete_enabled"
        const val KEY_PERMANENT_DELETE_WARN = "permanent_delete_warn"
        const val KEY_REVERSE_SWIPE = "reverse_swipe_enabled"
        const val KEY_USE_NEW_UI = "use_new_ui"
        const val KEY_APP_LANGUAGE = "app_language"
        const val LANGUAGE_SYSTEM = "system"
        const val LANGUAGE_FA = "fa"
        const val LANGUAGE_EN = "en"

        const val KEY_PREVIEW_LINES = "preview_lines"
        const val DEFAULT_PREVIEW_LINES = 1

        const val KEY_CUSTOM_CELL_COLOR = "custom_cell_color"
        const val CELL_COLOR_THEME = "theme"

        const val KEY_CELL_STYLE = "cell_style"
        const val CELL_STYLE_CARD = "card"
        const val CELL_STYLE_DIVIDER = "divider"

        const val KEY_CELL_BORDER_OPACITY = "cell_border_opacity"
        const val BORDER_OPACITY_SUBTLE = "subtle"
        const val BORDER_OPACITY_MEDIUM = "medium"
        const val BORDER_OPACITY_STRONG = "strong"
        const val BORDER_OPACITY_NEON = "neon"

        const val KEY_CELL_BORDER_OPACITY_FLOAT = "cell_border_opacity_float"
        const val KEY_CELL_BORDER_WIDTH_FLOAT = "cell_border_width_float"
        const val KEY_MESSAGE_CARD_SHAPE = "message_card_shape"
        const val SHAPE_ROUNDED = "rounded"
        const val SHAPE_CYBER = "cyber"
        const val SHAPE_BUBBLE = "bubble"
        const val SHAPE_MINIMAL = "minimal"
        const val SHAPE_CAPSULE = "capsule"

        const val KEY_DIVIDER_COLOR = "divider_color"

        const val KEY_CALENDAR_TYPE = "calendar_type"
        const val CALENDAR_JALALI = "jalali"
        const val CALENDAR_GREGORIAN = "gregorian"

        @Volatile
        private var instance: SettingsStore? = null

        fun get(context: Context): SettingsStore =
            instance ?: synchronized(this) {
                instance ?: SettingsStore(context.applicationContext).also { instance = it }
            }

        const val KEY_LAUNCHER_ICON = "launcher_icon"
        const val ICON_BLACK_WHITE = "black_white"
        const val ICON_STARK_GOLD = "stark_gold"
        const val ICON_MATRIX_GREEN = "matrix_green"
        const val ICON_CYBER_CYAN = "cyber_cyan"

        const val KEY_HIDE_NOTIF_BODY = "hide_notif_body"
        const val KEY_DRAFTS_ENABLED = "drafts_enabled"
        const val KEY_SWIPE_LEFT_ACTION = "swipe_left_action"
        const val KEY_SWIPE_RIGHT_ACTION = "swipe_right_action"
        const val KEY_LINK_WARNING = "link_open_warning_enabled"
        const val KEY_FONT_FAMILY = "font_family"
        const val FONT_SYSTEM = "system"
        const val FONT_DM_SANS = "dm_sans"
        const val FONT_INTER = "inter"
        const val FONT_FIGTREE = "figtree"
        const val FONT_POPPINS = "poppins"
        const val KEY_BLOCKED_KEYWORDS = "blocked_keywords"
        const val KEY_BACKUP_TREE_URI = "backup_tree_uri"
        const val KEY_PERIODIC_BACKUP = "periodic_backup_enabled"
        const val KEY_PERIODIC_BACKUP_INTERVAL = "periodic_backup_interval"
        const val KEY_LAST_BACKUP_AT = "last_backup_at"
        const val KEY_LAST_BACKUP_OK = "last_backup_ok"
        const val KEY_LAST_BACKUP_DETAIL = "last_backup_detail"
        const val KEY_A11Y_ENABLED = "a11y_enabled"
        const val KEY_A11Y_FONT_SCALE = "a11y_font_scale"
        const val KEY_A11Y_BOLD = "a11y_bold"
        const val KEY_A11Y_HIGH_CONTRAST = "a11y_high_contrast"
        const val KEY_A11Y_REDUCE_MOTION = "a11y_reduce_motion"
        const val KEY_A11Y_LARGE_TOUCH = "a11y_large_touch"
        const val A11Y_FONT_DEFAULT = 100
        const val A11Y_FONT_MIN = 85
        const val A11Y_FONT_MAX = 130
        const val DEFAULTS_NOTIFICATIONS = true
        const val DEFAULTS_SOUNDS = true
        const val DEFAULTS_DELIVERY = false
        const val DEFAULTS_SIM_SUBSCRIPTION_ID = -1
        const val DEFAULTS_EMOJI_BUTTON = false
        const val DEFAULTS_UNREAD_AT_TOP = false
    }

    var vipThemesUnlocked: Boolean
        get() = prefs.getBoolean(KEY_VIP_THEMES_UNLOCKED, false)
        set(v) { prefs.edit().putBoolean(KEY_VIP_THEMES_UNLOCKED, v).apply(); _revision.value++ }

    fun unlockVipThemesWithToken(token: String): Boolean {
        val trimmed = token.trim()
        if (trimmed == VIP_TOKEN_1 || trimmed == VIP_TOKEN_2) {
            vipThemesUnlocked = true
            return true
        }
        return false
    }

    var themeMode: String
        get() = prefs.getString(KEY_THEME, THEME_STARK) ?: THEME_STARK
        set(v) { prefs.edit().putString(KEY_THEME, v).apply(); _revision.value++ }

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, DEFAULTS_NOTIFICATIONS)
        set(v) { prefs.edit().putBoolean(KEY_NOTIFICATIONS, v).apply(); _revision.value++ }

    var soundsEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUNDS, DEFAULTS_SOUNDS)
        set(v) { prefs.edit().putBoolean(KEY_SOUNDS, v).apply(); _revision.value++ }

    var deliveryReportsEnabled: Boolean
        get() = prefs.getBoolean(KEY_DELIVERY_REPORTS, DEFAULTS_DELIVERY)
        set(v) { prefs.edit().putBoolean(KEY_DELIVERY_REPORTS, v).apply(); _revision.value++ }

    var simSubscriptionId: Int
        get() = prefs.getInt(KEY_SIM_SUBSCRIPTION_ID, DEFAULTS_SIM_SUBSCRIPTION_ID)
        set(v) { prefs.edit().putInt(KEY_SIM_SUBSCRIPTION_ID, v).apply(); _revision.value++ }

    var firstImportDone: Boolean
        get() = prefs.getBoolean(KEY_FIRST_IMPORT_DONE, false)
        set(v) { prefs.edit().putBoolean(KEY_FIRST_IMPORT_DONE, v).apply(); _revision.value++ }

    var participantsMigrated: Boolean
        get() = prefs.getBoolean(KEY_PARTICIPANTS_MIGRATED, false)
        set(v) { prefs.edit().putBoolean(KEY_PARTICIPANTS_MIGRATED, v).apply() }

    var alphanumericRepairDone: Boolean
        get() = prefs.getBoolean(KEY_ALPHANUMERIC_REPAIR_DONE, false)
        set(v) { prefs.edit().putBoolean(KEY_ALPHANUMERIC_REPAIR_DONE, v).apply() }

    var phoneRegion: String
        get() = prefs.getString(KEY_PHONE_REGION, "") ?: ""
        set(v) { prefs.edit().putString(KEY_PHONE_REGION, v).apply() }

    var pinnedEnabled: Boolean
        get() = prefs.getBoolean(KEY_PINNED_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_PINNED_ENABLED, v).apply(); _revision.value++ }

    var archivingEnabled: Boolean
        get() = prefs.getBoolean(KEY_ARCHIVING_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_ARCHIVING_ENABLED, v).apply(); _revision.value++ }

    var blockingEnabled: Boolean
        get() = prefs.getBoolean(KEY_BLOCKING_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_BLOCKING_ENABLED, v).apply(); _revision.value++ }

    var retentionTrashDays: Int
        get() = RetentionPolicy.normalizedDays(
            prefs.getInt(KEY_RETENTION_TRASH_DAYS, RetentionPolicy.DEFAULT_DAYS)
        )
        set(v) {
            prefs.edit().putInt(KEY_RETENTION_TRASH_DAYS, RetentionPolicy.normalizedDays(v)).apply()
            _revision.value++
        }

    /** Covers both Spam & Blocked buckets, which are one folder. */
    var retentionSpamDays: Int
        get() = RetentionPolicy.normalizedDays(
            prefs.getInt(KEY_RETENTION_SPAM_DAYS, RetentionPolicy.DEFAULT_DAYS)
        )
        set(v) {
            prefs.edit().putInt(KEY_RETENTION_SPAM_DAYS, RetentionPolicy.normalizedDays(v)).apply()
            _revision.value++
        }

    var retentionEnabled: Boolean
        get() = prefs.getBoolean(KEY_RETENTION_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_RETENTION_ENABLED, v).apply(); _revision.value++ }

    var retentionTrash: Boolean
        get() = prefs.getBoolean(KEY_RETENTION_TRASH, true)
        set(v) { prefs.edit().putBoolean(KEY_RETENTION_TRASH, v).apply(); _revision.value++ }

    var retentionKeywordMessages: Boolean
        get() = prefs.getBoolean(KEY_RETENTION_KEYWORD, true)
        set(v) { prefs.edit().putBoolean(KEY_RETENTION_KEYWORD, v).apply(); _revision.value++ }

    var retentionBlockedSenders: Boolean
        get() = prefs.getBoolean(KEY_RETENTION_BLOCKED, true)
        set(v) { prefs.edit().putBoolean(KEY_RETENTION_BLOCKED, v).apply(); _revision.value++ }

    var forwardingEnabled: Boolean
        get() = prefs.getBoolean(KEY_FORWARDING_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_FORWARDING_ENABLED, v).apply(); _revision.value++ }

    /**
     * Which settings/chat surface to render. The pre-redesign screens under
     * `ui/legacy` are the default; switching on renders the redesigned set.
     * Both read this same store, so data stays in sync either way.
     */
    var useNewUi: Boolean
        get() = prefs.getBoolean(KEY_USE_NEW_UI, true)
        set(v) { prefs.edit().putBoolean(KEY_USE_NEW_UI, v).apply(); _revision.value++ }

    var appLanguage: String
        get() = prefs.getString(KEY_APP_LANGUAGE, LANGUAGE_SYSTEM) ?: LANGUAGE_SYSTEM
        set(v) { prefs.edit().putString(KEY_APP_LANGUAGE, v).apply(); _revision.value++ }

    var previewLines: Int
        get() = prefs.getInt(KEY_PREVIEW_LINES, DEFAULT_PREVIEW_LINES).coerceIn(1, 3)
        set(v) { prefs.edit().putInt(KEY_PREVIEW_LINES, v.coerceIn(1, 3)).apply(); _revision.value++ }

    var customCellColor: String
        get() = prefs.getString(KEY_CUSTOM_CELL_COLOR, CELL_COLOR_THEME) ?: CELL_COLOR_THEME
        set(v) { prefs.edit().putString(KEY_CUSTOM_CELL_COLOR, v).apply(); _revision.value++ }

    var hideNotificationBody: Boolean
        get() = prefs.getBoolean(KEY_HIDE_NOTIF_BODY, false)
        set(v) { prefs.edit().putBoolean(KEY_HIDE_NOTIF_BODY, v).apply(); _revision.value++ }

    var cellStyle: String
        get() = prefs.getString(KEY_CELL_STYLE, CELL_STYLE_CARD) ?: CELL_STYLE_CARD
        set(v) { prefs.edit().putString(KEY_CELL_STYLE, v).apply(); _revision.value++ }

    var cellBorderOpacity: String
        get() = prefs.getString(KEY_CELL_BORDER_OPACITY, BORDER_OPACITY_MEDIUM) ?: BORDER_OPACITY_MEDIUM
        set(v) { prefs.edit().putString(KEY_CELL_BORDER_OPACITY, v).apply(); _revision.value++ }

    var cellBorderOpacityFloat: Float
        get() = prefs.getFloat(KEY_CELL_BORDER_OPACITY_FLOAT, 0.45f)
        set(v) { prefs.edit().putFloat(KEY_CELL_BORDER_OPACITY_FLOAT, v.coerceIn(0.05f, 1.0f)).apply(); _revision.value++ }

    var cellBorderWidthFloat: Float
        get() = prefs.getFloat(KEY_CELL_BORDER_WIDTH_FLOAT, 0.8f)
        set(v) { prefs.edit().putFloat(KEY_CELL_BORDER_WIDTH_FLOAT, v.coerceIn(0.3f, 4.0f)).apply(); _revision.value++ }

    var messageCardShape: String
        get() = prefs.getString(KEY_MESSAGE_CARD_SHAPE, SHAPE_ROUNDED) ?: SHAPE_ROUNDED
        set(v) { prefs.edit().putString(KEY_MESSAGE_CARD_SHAPE, v).apply(); _revision.value++ }

    var calendarType: String
        get() = prefs.getString(KEY_CALENDAR_TYPE, CALENDAR_JALALI) ?: CALENDAR_JALALI
        set(v) { prefs.edit().putString(KEY_CALENDAR_TYPE, v).apply(); _revision.value++ }

    var dividerColor: String
        get() = prefs.getString(KEY_DIVIDER_COLOR, CELL_COLOR_THEME) ?: CELL_COLOR_THEME
        set(v) { prefs.edit().putString(KEY_DIVIDER_COLOR, v).apply(); _revision.value++ }

    var launcherIcon: String
        get() = prefs.getString(KEY_LAUNCHER_ICON, ICON_BLACK_WHITE) ?: ICON_BLACK_WHITE
        set(v) { prefs.edit().putString(KEY_LAUNCHER_ICON, v).apply(); _revision.value++ }

    /** Legacy chat surfaces still gate on this. */
    var draftsEnabled: Boolean
        get() = prefs.getBoolean(KEY_DRAFTS_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_DRAFTS_ENABLED, v).apply(); _revision.value++ }

    /**
     * The legacy master "swipe actions" switch.
     *
     * It is a view over the per-direction actions — the single source of truth —
     * so the legacy and the new settings cannot disagree. Off turns both
     * directions off; on restores the pre-per-direction pair, honouring the
     * legacy reverse setting.
     */
    var swipeActionsEnabled: Boolean
        get() = SwipeAction.anyEnabled(swipeLeftAction, swipeRightAction)
        set(v) {
            val (left, right) = SwipeAction.legacyPair(v, reverseSwipeEnabled)
            swipeLeftAction = left
            swipeRightAction = right
        }

    var reverseSwipeEnabled: Boolean
        get() = prefs.getBoolean(KEY_REVERSE_SWIPE, false)
        set(v) { prefs.edit().putBoolean(KEY_REVERSE_SWIPE, v).apply(); _revision.value++ }

    var unreadAtTopEnabled: Boolean
        get() = prefs.getBoolean(KEY_UNREAD_AT_TOP_ENABLED, DEFAULTS_UNREAD_AT_TOP)
        set(v) { prefs.edit().putBoolean(KEY_UNREAD_AT_TOP_ENABLED, v).apply(); _revision.value++ }

    var scheduledMessagesEnabled: Boolean
        get() = prefs.getBoolean(KEY_SCHEDULED_MESSAGES_ENABLED, true)
        set(v) { prefs.edit().putBoolean(KEY_SCHEDULED_MESSAGES_ENABLED, v).apply(); _revision.value++ }

    var delayedSendingEnabled: Boolean
        get() = prefs.getBoolean(KEY_DELAYED_SENDING_ENABLED, false)
        set(v) { prefs.edit().putBoolean(KEY_DELAYED_SENDING_ENABLED, v).apply(); _revision.value++ }

    var highlightLinks: Boolean
        get() = prefs.getBoolean(KEY_HIGHLIGHT_LINKS, true)
        set(v) {
            val editor = prefs.edit().putBoolean(KEY_HIGHLIGHT_LINKS, v)
            if (!v) editor.putBoolean(KEY_LINK_WARNING, false)
            editor.apply()
            _revision.value++
        }

    var hideLinks: Boolean
        get() = prefs.getBoolean(KEY_HIDE_LINKS, false)
        set(v) {
            val editor = prefs.edit().putBoolean(KEY_HIDE_LINKS, v)
            if (v) {
                editor.putBoolean(KEY_HIGHLIGHT_LINKS, false)
                editor.putBoolean(KEY_LINK_WARNING, false)
            }
            editor.apply()
            _revision.value++
        }

    var privacyModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_PRIVACY_MODE, false)
        set(v) { prefs.edit().putBoolean(KEY_PRIVACY_MODE, v).apply(); _revision.value++ }

    var appLockEnabled: Boolean
        get() = prefs.getBoolean(KEY_APP_LOCK, false)
        set(v) { prefs.edit().putBoolean(KEY_APP_LOCK, v).apply(); _revision.value++ }

    var delaySeconds: Int
        get() = prefs.getInt(KEY_DELAY_SECONDS, 3)
        set(v) { prefs.edit().putInt(KEY_DELAY_SECONDS, v).apply(); _revision.value++ }

    var sendSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SEND_SOUND, DEFAULTS_SOUNDS)
        set(v) { prefs.edit().putBoolean(KEY_SEND_SOUND, v).apply(); _revision.value++ }

    var receiveSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_RECEIVE_SOUND, DEFAULTS_SOUNDS)
        set(v) { prefs.edit().putBoolean(KEY_RECEIVE_SOUND, v).apply(); _revision.value++ }

    var notificationSound: String
        get() = prefs.getString(KEY_NOTIFICATION_SOUND, NOTIFY_SOUND_DEFAULT) ?: NOTIFY_SOUND_DEFAULT
        set(v) { prefs.edit().putString(KEY_NOTIFICATION_SOUND, v).apply(); _revision.value++ }

    /** Which actions an incoming-message notification offers. All on by default. */
    var notifActionReply: Boolean
        get() = prefs.getBoolean(KEY_NOTIF_ACTION_REPLY, true)
        set(v) { prefs.edit().putBoolean(KEY_NOTIF_ACTION_REPLY, v).apply(); _revision.value++ }

    var notifActionMarkRead: Boolean
        get() = prefs.getBoolean(KEY_NOTIF_ACTION_MARK_READ, true)
        set(v) { prefs.edit().putBoolean(KEY_NOTIF_ACTION_MARK_READ, v).apply(); _revision.value++ }

    var notifActionDelete: Boolean
        get() = prefs.getBoolean(KEY_NOTIF_ACTION_DELETE, true)
        set(v) { prefs.edit().putBoolean(KEY_NOTIF_ACTION_DELETE, v).apply(); _revision.value++ }

    var showSimIndicator: Boolean
        get() = prefs.getBoolean(KEY_SHOW_SIM_INDICATOR, true)
        set(v) { prefs.edit().putBoolean(KEY_SHOW_SIM_INDICATOR, v).apply(); _revision.value++ }

    var emojiButtonEnabled: Boolean
        get() = prefs.getBoolean(KEY_EMOJI_BUTTON, DEFAULTS_EMOJI_BUTTON)
        set(v) { prefs.edit().putBoolean(KEY_EMOJI_BUTTON, v).apply(); _revision.value++ }

    var permanentDeleteEnabled: Boolean
        get() = prefs.getBoolean(KEY_PERMANENT_DELETE, false)
        set(v) { prefs.edit().putBoolean(KEY_PERMANENT_DELETE, v).apply(); _revision.value++ }

    var permanentDeleteWarn: Boolean
        get() = prefs.getBoolean(KEY_PERMANENT_DELETE_WARN, true)
        set(v) { prefs.edit().putBoolean(KEY_PERMANENT_DELETE_WARN, v).apply(); _revision.value++ }

    /**
     * Left/right swipe actions. The first read on an install that predates these
     * keys derives them from `swipe_actions_enabled` + `reverse_swipe_enabled`,
     * so upgrading users keep the behaviour they had.
     */
    var swipeLeftAction: SwipeAction
        get() = prefs.getInt(KEY_SWIPE_LEFT_ACTION, -1).takeIf { it >= 0 }?.let(SwipeAction::fromStorage)
            ?: migrateSwipeActions().first
        set(v) { prefs.edit().putInt(KEY_SWIPE_LEFT_ACTION, v.storageValue).apply(); _revision.value++ }

    var swipeRightAction: SwipeAction
        get() = prefs.getInt(KEY_SWIPE_RIGHT_ACTION, -1).takeIf { it >= 0 }?.let(SwipeAction::fromStorage)
            ?: migrateSwipeActions().second
        set(v) { prefs.edit().putInt(KEY_SWIPE_RIGHT_ACTION, v.storageValue).apply(); _revision.value++ }

    /** Writes the derived pair once, then the getters read the new keys. */
    private fun migrateSwipeActions(): Pair<SwipeAction, SwipeAction> {
        val (left, right) = SwipeAction.legacyPair(
            enabled = prefs.getBoolean(KEY_SWIPE_ACTIONS_ENABLED, true),
            reverse = prefs.getBoolean(KEY_REVERSE_SWIPE, false)
        )
        prefs.edit()
            .putInt(KEY_SWIPE_LEFT_ACTION, left.storageValue)
            .putInt(KEY_SWIPE_RIGHT_ACTION, right.storageValue)
            .apply()
        return left to right
    }

    var linkOpenWarningEnabled: Boolean
        get() = prefs.getBoolean(KEY_LINK_WARNING, true)
        set(v) {
            val enabled = v && highlightLinks && !hideLinks
            prefs.edit().putBoolean(KEY_LINK_WARNING, enabled).apply()
            _revision.value++
        }

    var fontFamily: String
        get() = prefs.getString(KEY_FONT_FAMILY, FONT_SYSTEM) ?: FONT_SYSTEM
        set(v) { prefs.edit().putString(KEY_FONT_FAMILY, v).apply(); _revision.value++ }

    var blockedKeywords: Set<String>
        get() = prefs.getStringSet(KEY_BLOCKED_KEYWORDS, emptySet()) ?: emptySet()
        set(v) { prefs.edit().putStringSet(KEY_BLOCKED_KEYWORDS, v).apply(); _revision.value++ }

    /** Master switch for accessibility mode; when off every a11y option is ignored. */
    var a11yEnabled: Boolean
        get() = prefs.getBoolean(KEY_A11Y_ENABLED, false)
        set(v) { prefs.edit().putBoolean(KEY_A11Y_ENABLED, v).apply(); _revision.value++ }

    /** App-level text scale as a percentage applied on top of the system font scale. */
    var a11yFontScalePercent: Int
        get() = prefs.getInt(KEY_A11Y_FONT_SCALE, A11Y_FONT_DEFAULT)
        set(v) {
            prefs.edit().putInt(KEY_A11Y_FONT_SCALE, v.coerceIn(A11Y_FONT_MIN, A11Y_FONT_MAX)).apply()
            _revision.value++
        }

    var a11yBold: Boolean
        get() = prefs.getBoolean(KEY_A11Y_BOLD, false)
        set(v) { prefs.edit().putBoolean(KEY_A11Y_BOLD, v).apply(); _revision.value++ }

    var a11yHighContrast: Boolean
        get() = prefs.getBoolean(KEY_A11Y_HIGH_CONTRAST, false)
        set(v) { prefs.edit().putBoolean(KEY_A11Y_HIGH_CONTRAST, v).apply(); _revision.value++ }

    var a11yReduceMotion: Boolean
        get() = prefs.getBoolean(KEY_A11Y_REDUCE_MOTION, false)
        set(v) { prefs.edit().putBoolean(KEY_A11Y_REDUCE_MOTION, v).apply(); _revision.value++ }

    var a11yLargeTouch: Boolean
        get() = prefs.getBoolean(KEY_A11Y_LARGE_TOUCH, false)
        set(v) { prefs.edit().putBoolean(KEY_A11Y_LARGE_TOUCH, v).apply(); _revision.value++ }

    /** Persisted SAF tree URI for backups; empty means the default Documents/Messages. */
    var backupTreeUri: String
        get() = prefs.getString(KEY_BACKUP_TREE_URI, "") ?: ""
        set(v) { prefs.edit().putString(KEY_BACKUP_TREE_URI, v).apply(); _revision.value++ }

    /** Automatic backup on a schedule (issue #290). Off until the user opts in. */
    var periodicBackupEnabled: Boolean
        get() = prefs.getBoolean(KEY_PERIODIC_BACKUP, false)
        set(v) { prefs.edit().putBoolean(KEY_PERIODIC_BACKUP, v).apply(); _revision.value++ }

    /** [PeriodicBackupScheduler.INTERVAL_DAILY] or `weekly`. */
    var periodicBackupInterval: String
        get() = prefs.getString(KEY_PERIODIC_BACKUP_INTERVAL, PeriodicBackupScheduler.DEFAULT_INTERVAL)
            ?: PeriodicBackupScheduler.DEFAULT_INTERVAL
        set(v) { prefs.edit().putString(KEY_PERIODIC_BACKUP_INTERVAL, v).apply(); _revision.value++ }

    /** Last completed automatic backup attempt, in epoch millis; 0 if none. */
    var lastBackupAt: Long
        get() = prefs.getLong(KEY_LAST_BACKUP_AT, 0L)
        set(v) { prefs.edit().putLong(KEY_LAST_BACKUP_AT, v).apply(); _revision.value++ }

    /** Whether the last automatic backup attempt succeeded. */
    var lastBackupOk: Boolean
        get() = prefs.getBoolean(KEY_LAST_BACKUP_OK, true)
        set(v) { prefs.edit().putBoolean(KEY_LAST_BACKUP_OK, v).apply(); _revision.value++ }

    /** Outcome detail of the last automatic backup, for diagnostics. */
    var lastBackupDetail: String
        get() = prefs.getString(KEY_LAST_BACKUP_DETAIL, "") ?: ""
        set(v) { prefs.edit().putString(KEY_LAST_BACKUP_DETAIL, v).apply(); _revision.value++ }

    /** True when [body] contains any blocked keyword (case-insensitive). */
    fun isKeywordBlocked(body: String): Boolean = KeywordFilter.isBlocked(body, blockedKeywords)
}
