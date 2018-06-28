package org.geogebra.web.shared;

import org.geogebra.common.main.Feature;
import org.geogebra.common.move.ggtapi.models.GeoGebraTubeUser;
import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.gui.FastClickHandler;
import org.geogebra.web.html5.gui.GPopupPanel;
import org.geogebra.web.html5.gui.util.StandardButton;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.main.LocalizationW;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ProfilePanel extends FlowPanel {
	private Image profileImage;
	private FlowPanel optionsPanel;
	private FlowPanel arrowPanel;
	private FlowPanel optionsPanelContent;
	private StandardButton logoutButton;
	private LocalizationW loc;
	private Anchor username;
	private Anchor editProfile;

	/**
	 * @param app
	 *            application
	 */
	public ProfilePanel(final AppW app) {
		setStyleName("profilePanel");
		this.loc = app.getLocalization();
		this.profileImage = new Image();
		this.profileImage.setStyleName("profileImage");
		this.profileImage.setHeight("40px");
		add(this.profileImage);

		final GPopupPanel popup = new GPopupPanel(app.getPanel(), app);
		popup.addStyleName("optionsPopup");
		if (app.has(Feature.DIALOG_DESIGN)) {
			// TODO mat design
			// popup.addStyleName("MaterialDialogBox");
		}
		popup.setAutoHideEnabled(true);
		popup.addAutoHidePartner(getElement());
		this.optionsPanel = new FlowPanel();
		this.optionsPanel.setStyleName("profileOptionsPanel");

		arrowPanel = new FlowPanel();
		Image arrow = new Image(SharedResources.INSTANCE.arrow_submenu_up());
		arrowPanel.add(arrow);
		arrowPanel.setStyleName("arrow");
		optionsPanel.add(arrowPanel);

		optionsPanelContent = new FlowPanel();
		optionsPanelContent.setStyleName("profileOptionsContent");
		optionsPanel.add(optionsPanelContent);

		logoutButton = new StandardButton("", app);
		logoutButton.addStyleName("logoutButton");
		logoutButton.addStyleName("gwt-Button");
		username = addLink("username");
		editProfile = addLink("editButton");
		optionsPanelContent.add(editProfile);
		optionsPanelContent.add(logoutButton);

		logoutButton.addFastClickHandler(new FastClickHandler() {

			@Override
			public void onClick(Widget source) {
				Log.debug("logout");
				app.getLoginOperation().performLogOut();
				togglePopup(popup);
			}
		});

		popup.add(optionsPanel);
		addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				togglePopup(popup);
				event.stopPropagation();
			}
		}, ClickEvent.getType());
		setLabels();
	}

	private Anchor addLink(String className) {
		Anchor ret = new Anchor();
		ret.setTarget("_blank");
		ret.setStyleName(className);
		optionsPanelContent.add(ret);
		return ret;
	}

	/**
	 * show / hide popupPanel.
	 * 
	 * @param p
	 *            PopupPanel
	 */
	void togglePopup(GPopupPanel p) {
		if (p.isShowing()) {
			p.hide();
		} else {
			p.showRelativeTo(this);
		}
	}

	/**
	 * Update profile pic
	 * 
	 * @param user
	 *            signed in user
	 */
	public void update(GeoGebraTubeUser user) {
		username.setHref(user.getProfileURL());
		username.setText(user.getRealName());
		if (user.getImageURL() != null) {
			this.profileImage.setUrl(user.getImageURL());
		} else {
			this.profileImage.setUrl(
					SharedResources.INSTANCE.icon_help_black().getSafeUri());
		}
	}

	/**
	 * Update localization
	 */
	public void setLabels() {
		if (this.logoutButton != null) {
			this.logoutButton.setText(loc.getMenu("SignOut"));
		}
		if (this.editProfile != null) {
			this.editProfile.setText(loc.getMenu("EditProfile"));
		}
	}
}
