package com.example.cadpart;


import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
@StyleSheet("../../../VAADIN/themes/mytheme/login.css") //http://localhost:8080/vaadinServlet/APP/PUBLISHED//VAADIN/themes/mytheme/sliderpanel.css
public class LoginView extends CustomComponent implements View {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Login";

	public LoginView(){
		setSizeFull();
		addStyleName("login-background"); 
		
		
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();

		
		
		FormLayout content = new FormLayout();
		TextField username = new TextField("Username");
		content.addComponent(username);
		PasswordField password = new PasswordField("Password");
		content.addComponent(password);

		Button send = new Button("Enter");
		send.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(LoginUI.AUTH.authenticate(username.getValue(), password.getValue())){
					VaadinSession.getCurrent().setAttribute("user", username.getValue());
					
					getUI().getNavigator().navigateTo(CADPartLandingView.NAME);
					//getUI().getNavigator().navigateTo(OtherSecurePage.NAME);
					//Page.getCurrent().setUriFragment("!"+CADPartGeoView.NAME);
				}else{
					Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
				}
			}
			
		});
		content.addComponent(send);
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);
		
        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(panel);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
       // viewLayout.setStyleName(Valo.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);		
		
		
	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
