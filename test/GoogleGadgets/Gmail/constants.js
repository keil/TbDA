// Copyright 2007 Google Inc.
// All Rights Reserved.

// @fileoverview Constants used throughout the plugin

var TIME_BETWEEN_HTTP_REQUESTS = 1000; // in ms
var SWITCH_COMBO_SCROLL_DELAY = 100;
var BLANK_LINE = 'b';
var END_LINE_CHAR = String.fromCharCode(10);

var SENDER_COLOR_PALETTE = ['#790619', '#ad3894', '#c8892b', '#e62b60',
                            '#c580de', '#558391', '#a60619', '#c8b080',
                            '#d50060', '#80a686', '#c28000', '#846655',
                            '#cc55ca', '#550099', '#55681c', '#79063f',
                            '#da8900', '#cc007b', '#008391'];

var REPORTED_CLIENT_NAME = 'gd-gmail-gadget-' + VERSION_STRING;

// All the possible folders
var FILTERS = {
    INBOX: 'INBOX',
    STARRED: 'label:Starred',
    CHATS: 'label:Chats',
    SENT_MAIL: 'label:Sent',
    ALL_MAIL: 'label:',
    SPAM: 'label:Spam',
    TRASH: 'label:Trash',
    SEARCH: 'SEARCH',
    LABEL: 'LABEL'};

// The following constants define the positions of various views in the
// view switch combobox

// JS compiler does not like assignment to CONSTANTS.
// Declaring with a regular variable first.
var tempComboPosition = {};
tempComboPosition['INBOX'] = 0;
tempComboPosition[FILTERS.STARRED] = 1;
tempComboPosition[FILTERS.ALL_MAIL] = 4;
tempComboPosition[1] = FILTERS.STARRED;
tempComboPosition[2] = FILTERS.CHATS;
tempComboPosition[3] = FILTERS.SENT_MAIL;
tempComboPosition[4] = FILTERS.ALL_MAIL;
tempComboPosition[5] = FILTERS.SPAM;
tempComboPosition[6] = FILTERS.TRASH;
tempComboPosition['LABELS_SEPARATOR'] = 7;

var COMBO_POSITION = tempComboPosition;

var SOURCE_ID = 'GD';

var CONNECTION_DATA = {
    // Gmail URL with a random number to avoid carrier network caching
    GMAIL_URL: '/mail/m/' + Math.floor(Math.random() * 10000000000) +
        '?source=' + SOURCE_ID + '&ver=' + VERSION_STRING,
    APPS_URL: '/m/' + Math.floor(Math.random() * 10000000000) +
        '?source=' + SOURCE_ID + '&ver=' + VERSION_STRING,
    COMMON_POST_PARAMETERS: 'p=1.1&x=Web&platformID=' + SOURCE_ID +
      '&xv=' + VERSION_STRING,
    REFRESH_INTERVAL_MS: 300000,  // 5 minutes in milliseconds
    LOADED_STATE: 4,  // loaded state number for xmlhttprequest
    HTTP_OK: 200,  // ready status number for xmlhttprequest
    GMAIL_HOST: 'mail.google.com',
    APPS_DOMAIN_PREFIX: 'mail.google.com/a/',
    HTTP_PREFIX: 'http://',
    HTTPS_PREFIX: 'https://',
    MOBILE_AUTH_HEADER: 'X-Mobile-Google-Client',
    MOBILE_AUTH_HEADER_VALUE: '1' };

var ACTION = {
    ARCHIVE: 1,
    TRASH: 2,
    REPORT_SPAM: 3,
    STAR: 4,
    UNSTAR: 5,
    MARK_AS_READ: 6,
    MARK_AS_UNREAD: 7};

// These are constant strings from the milu request/response format
var URL = {
    MODE: 'zym',
    LOGIN_MODE: 'l',
    OFFSET_PARAM: 'st',
    NUM_MAILS_PARAM: 'sz',
    USER_PARAM: 'user',
    PASS_PARAM: 'password',
    TIMESTAMP_PARAM: 'tlt',
    SEARCH_LABEL_PARAM: 's',
    SEARCH_MODE: 'q',
    LABEL_MODE: 'cat',
    ACTION_TOKEN_PARAM: 'at',
    TO_PARAM: 'to',
    CC_PARAM: 'cc',
    BCC_PARAM: 'bcc',
    SUBJECT_PARAM: 'subject',
    BODY_PARAM: 'body',
    BU_PARAM: 'bu',
    HAOT_PARAM: 'haot',
    MESSAGE_URL_PARAM: 'rm',
    CONV_TITLE_PARAM: 'rmns',
    THREAD_ID_PARAM: 'th',
    SEND_OPERATION: 'sfc',
    QUOTE_THREAD: 'qt',
    FORWARD_THREAD: 'ft',
    VIEW_PARAM: 'v',
    ATTACHMENT_VIEW_PARAM: 'view',
    ATTACHMENT_VIEW: 'att',
    ATTACHMENT_DISPLAY_PARAM: 'disp',
    ATTACHMENT_DISPLAY: 'vah',
    ATTACHMENT_MAX_WIDTH: 'w',
    ATTACHMENT_MAX_HEIGHT: 'h',
    ATTACHMENT_ID_PARAM: 'attid',
    JPEG_SUPPORT_PARAM: 'j',
    CONTACTS_LIST: 'cl',
    CONVERSATION_VIEW: 'c',
    CONTACTS_LIST_ORDERING: 'pnl',
    CL_ALL: 'a',
    CL_FREQUENTLY_USED: 'f',
    CL_ALL_SORTED_FREQUENCY: 'ac',
    CL_FREQUENTLY_USED_SORTED_FREQ: 'fc',
    SPAM_PARAM: 'ssp',
    TRASH_PARAM: 'str',
    DISABLED: '0',
    ENABLED: '1',

    ACTION_PARAM: ['', 'ar', 'tr', 'sp', 'st', 'xst', 'rd', 'ur'],

    THREAD_PARAM: 't',
    MESSAGE_ID_PARAM: 'm',
    MESSAGE_PARAM: 'd',
    MESSAGE_EXPAND_OPERATION: 'u',
    MESSAGE_OFFSET_PARAM: 'n',
    LOGOUT_PARAM: 'gadget_logout',
    MESSAGE_OUTPUT_HTML_PARAM: 'moh' };

var IMAGE_PATHS = {
    STAR_ON: 'images/star_active.png',
    STAR_OFF: 'images/star_default.png',
    PAPERCLIP: 'images/paperclip.png',
    ERROR_LEFT: 'images/error_left.png',
    ERROR_CENTER: 'images/error_center.png',
    ERROR_RIGHT: 'images/error_right.png',
    SWITCH_SCROLL_UP: 'images/scroll_up_default.png',
    SWITCH_SCROLL_UP_HOVER: 'images/scroll_up_hover.png',
    SWITCH_SCROLL_DOWN: 'images/scroll_down_default.png',
    SWITCH_SCROLL_DOWN_HOVER: 'images/scroll_down_hover.png'};

var UIDATA = {
    FONT: 'arial',
    LOADING_LABEL_X_OFFSET: 55,
    PAPERCLIP_OFFSET: 27,
    STAR_OFFSET: 15,
    DATE_WIDTH: 45,
    FONT_SIZE_SUBJECT: 8,
    FONT_COLOR_SUBJECT: '#0065cd',
    FONT_SIZE_SENDER: 8,
    FONT_SIZE_DATE: 8,
    FONT_COLOR_MAIL: '#66B3FF',
    MAIL_FIRST_LINE_HEIGHT: 14,
    RESIZE_TIMEOUT: 500,
    LABELS_COLOR: '#33AA33',
    DARK_BORDER: '#000000',
    LIGHT_BORDER: '#7F9DB9',
    ENABLED_TEXTBOX_COLOR: '#000000',
    DISABLED_TEXTBOX_COLOR: '#BBBBBB',
    ACTION_COMBO_TEXT_SIZE: 8,
    ERROR_MESSAGE_MAX_DISPLAY_TIME: 3000, // in ms
    DOUBLE_KEY_SHORTCUTS_MAX_DELAY: 3000}; // in ms

var KEYS = {
    TAB: 9, ENTER: 13, SHIFT: 16, ESCAPE: 27, SPACE: 32,
    HOME: 36, END: 35,
    LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40,
    MENU: 93, SLASH: 191,
    EXCLAMATION_MARK: 33, POUND_SIGN: 35,
    A: 65, C: 67, D: 68, F: 70, G: 71, I: 73, J: 74,
    K: 75, N: 78, O: 79, P: 80, R: 82, S: 83, U: 85, Y: 89,
    PAGE_UP: 33, PAGE_DOWN: 34, F5: 116 };

var THREAD_LIST_DATA_INFO = {
    TRUE: 'T', FALSE: 'F', AM: 'a', PM: 'p',
    REQUIRE_HTTPS: 'T', OPTIONAL_UPGRADE: 'O', PAGE_BROADCAST: 'B',
    LOGIN_ERROR: 'E',
    LEGAL_CONVERSATION: 'C',
    MESSAGE_BODY_LINE_STARTING_CHARACTER: ':',
    NO_CHANGE: 'NO_CHANGE',
    NEED_UPGRADE: 'ERROR:NEED_UPGRADE',
    LOGIN_ERRORS: {B: BAD_CREDENTIALS_ERROR_MESSAGE,
        C: CAPTCHA_ERROR_MESSAGE,
        L: ACCOUNT_LOCKED_ERROR}};

var OPTION_NAMES = {
    USER: 'user',
    PASS: 'pass' };

var BASE_TEN = 10;
