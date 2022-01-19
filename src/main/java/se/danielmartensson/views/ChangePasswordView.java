package se.danielmartensson.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import se.danielmartensson.service.UserPasswordService;
import se.danielmartensson.tools.MenuLayout;

@Route(value = "changePassword", layout = MenuLayout.class)
public class ChangePasswordView extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserPasswordService userPasswordService;

	public ChangePasswordView() {
		// Create a text field, password field and a button
		TextField currentUserName = new TextField("Current username");
		PasswordField newPassword = new PasswordField("New password");
		Button save = new Button("Save new password");

		// Add a listener to the button
		save.addClickListener(e -> {
			if(userPasswordService.changePassword(currentUserName.getValue(), newPassword.getValue()))
				Notification.show("Success!", 3000, Position.BOTTOM_START);
			else
				Notification.show("User " + currentUserName.getValue() + " does not exist!", 3000, Position.BOTTOM_START);

		});

		add(currentUserName, newPassword, save);
	}
}
