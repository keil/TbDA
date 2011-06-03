function DetailsViewController() {
  this.openedId = null;
  this.detailsView = null;
}

/**
 * Opens a details view.
 * @param {string} id ID associated with a details view.
 *   this is used to distinguish one details view from another.
 * @param {DetailsView} detailsView DetailsView object.
 * @param {string} title Details view title.
 * @param {int} flags Details view flags.
 */
DetailsViewController.prototype.open = function(id, detailsView, title, flags) {
  plugin.ShowDetailsView(detailsView,
      title,
      flags,
      bind(this, this.onFeedback));

  // If an already opened DV is closed, onFeedback() may be called here.

  this.openedId = id;
  this.detailsView = detailsView;
};

DetailsViewController.prototype.onFeedback = function() {
  this.openedId = null;
  this.detailsView = null;
};

/**
 * Is details view open for a particular ID?
 * @param {string} id ID associated with the details view.
 */
DetailsViewController.prototype.isOpen = function(id) {
  return this.openedId == id;
};

/**
 * Ensures details view with a particular ID is closed.
 * @param {string} id ID associated with the details view.
 */
DetailsViewController.prototype.ensureClose = function(id) {
  if (this.isOpen(id)) {
    plugin.CloseDetailsView();
  }
};

DetailsViewController.prototype.close = function() {
  plugin.CloseDetailsView();
};
