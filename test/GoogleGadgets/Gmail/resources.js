function Resources() {
  this.cache = {};
}

// Returns the contents of a script file.
Resources.prototype.getText = function(filename) {
  var script = this.cache[filename] || gadget.storage.openText(filename);

  if (script) {
    this.cache[filename] = script;
  }

  return script;
};

// Returns the path of an extracted image.
Resources.prototype.getImage = function(filename) {
  var path = this.cache[filename] ||
      gadget.storage.extract('images/details/' + filename).
          replace(/\\/g, '/');

  if (path) {
    this.cache[filename] = path;
  }

  return path;
};

Resources.DETAILS_HTML_FILE = 'details.html';
Resources.COMPOSE_HTML_FILE = 'compose.html';
Resources.DETAILS_COMMON_JS_FILE = 'details_common.js';
Resources.DETAILS_COMPOSE_JS_FILE = 'details_compose.js';

Resources.DETAILS_VIEW_IMAGES = [
  'action_button.gif',
  'dv_top.gif',
  'dv_topleft.gif',
  'dv_topright.gif',
  'dv_bottom.gif',
  'dv_bottomleft.gif',
  'dv_bottomright.gif',
  'dv_bottom_short.gif',
  'dv_bottomleft_short.gif',
  'dv_bottomright_short.gif',
  'dv_left.gif',
  'dv_right.gif',
  'error_center.gif',
  'error_left.gif',
  'error_right.gif',
  'focus_arrow.gif',
  'star_active_pad.gif',
  'star_default_pad.gif' ];

Resources.prototype.makePlaceholder = function(s) {
  return '[![' + s + ']!]';
};

Resources.prototype.getDetailsHtml = function() {
  if (this.cache[Resources.DETAILS_HTML_FILE]) {
    return this.cache[Resources.DETAILS_HTML_FILE];
  }

  var detailsHtml = this.getText(Resources.DETAILS_HTML_FILE);

  detailsHtml = detailsHtml.replace(
      this.makePlaceholder(Resources.DETAILS_COMMON_JS_FILE),
      g_resources.getText(Resources.DETAILS_COMMON_JS_FILE));

  detailsHtml = detailsHtml.replace(
      this.makePlaceholder(Resources.DETAILS_COMPOSE_JS_FILE),
      g_resources.getText(Resources.DETAILS_COMPOSE_JS_FILE));

  for (var i = 0; i < Resources.DETAILS_VIEW_IMAGES.length; ++i) {
    var file = Resources.DETAILS_VIEW_IMAGES[i];
    detailsHtml = detailsHtml.replace(
        new RegExp(Utils.escapeRegexp(this.makePlaceholder(file)), 'g'),
        g_resources.getImage(file));
  }

  this.cache[Resources.DETAILS_HTML_FILE] = detailsHtml;

  return detailsHtml;
};

Resources.prototype.getComposeHtml = function() {
  if (this.cache[Resources.COMPOSE_HTML_FILE]) {
    return this.cache[Resources.COMPOSE_HTML_FILE];
  }

  var detailsHtml = this.getText(Resources.COMPOSE_HTML_FILE);

  detailsHtml = detailsHtml.replace(
      this.makePlaceholder(Resources.DETAILS_COMMON_JS_FILE),
      g_resources.getText(Resources.DETAILS_COMMON_JS_FILE));

  detailsHtml = detailsHtml.replace(
      this.makePlaceholder(Resources.DETAILS_COMPOSE_JS_FILE),
      g_resources.getText(Resources.DETAILS_COMPOSE_JS_FILE));

  for (var i = 0; i < Resources.DETAILS_VIEW_IMAGES.length; ++i) {
    var file = Resources.DETAILS_VIEW_IMAGES[i];
    detailsHtml = detailsHtml.replace(
        new RegExp(Utils.escapeRegexp(this.makePlaceholder(file)), 'g'),
        g_resources.getImage(file));
  }

  this.cache[Resources.COMPOSE_HTML_FILE] = detailsHtml;

  return detailsHtml;
};
