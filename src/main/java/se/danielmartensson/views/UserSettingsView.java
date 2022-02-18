package se.danielmartensson.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;

import se.danielmartensson.service.UserSettingsService;
import se.danielmartensson.tools.MenuLayout;

@Route(value = "userSettings", layout = MenuLayout.class)
public class UserSettingsView extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserSettingsService userPasswordService;
	
	@Value("${login.masterName}")
	private String masterName;
	
	@Value("${login.slaveName}")
	private String slaveName;
	
	@PostConstruct
	public void init() {
		// Create a text field, password field and a button
		Select<String> selectedUser = new Select<String>(masterName, slaveName);
		selectedUser.setLabel("Select user");
		PasswordField newPassword = new PasswordField("New password");
		Button save = new Button("Save new password");

		// Add a listener to the button
		save.addClickListener(e -> {
			// Check if it's selected
			if(selectedUser.isEmpty())
				return;

			// See if you have right to change the password
			if(userPasswordService.loggedInUserIsMaster()) {
				userPasswordService.changePassword(selectedUser.getValue(), newPassword.getValue());
				Notification.show("Success!", 3000, Position.BOTTOM_START);
			}else {
				Notification.show("You don't have master rights to change password!", 3000, Position.BOTTOM_START);
			}
		});

		add(selectedUser, newPassword, save);
	}

	public UserSettingsView() {
		
	}
}
