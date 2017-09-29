package com.example.cadpart;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@SuppressWarnings("serial")
@Theme("valo")
@StyleSheet("/VAADIN/themes/mytheme/sliderpanel.css") 
//@EnableAutoConfiguration
public class LoginUI extends UI {

//@WebServlet(value = "/*", asyncSupported = true)
//@VaadinServletConfiguration(productionMode = false, ui = VaadinloginUI.class)

	@Autowired
	SpringViewProvider viewProvider;	
	
	
	
	public static class MyLoginServlet extends VaadinServlet {
	}

	
	private SideMenu sideMenu = new SideMenu();
	private boolean logoVisible = false;
	private ThemeResource logo = new ThemeResource("images/linux-penguin.png");
	private String menuCaption = "SideMenu Add-on";
	
	public static Authentication AUTH;
	@Override
	protected void init(VaadinRequest request) {
		
		setContent(sideMenu);
		
		AUTH = new Authentication();
		Navigator nav = new Navigator(this, sideMenu);
		nav.addProvider(viewProvider);
		setNavigator(nav);

		nav.addView("cadpartgeoview", new CADPartGeoView());

		
		
		
		nav.addView(LoginView.NAME, LoginView.class);
		nav.setErrorView(LoginView.class);
		
		Page.getCurrent().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				router(event.getUriFragment());
			}
		});
		
		sideMenu.setMenuCaption(menuCaption);

		// Navigation examples
		
		sideMenu.addNavigation("Search", FontAwesome.BARCODE, CADPartLandingView.NAME);
		sideMenu.addNavigation("CADPart", FontAwesome.GEARS, CADPartGeoView.NAME);

		// Arbitrary method execution
		sideMenu.addMenuItem("My Menu Entry", () -> {
			
			getNavigator().navigateTo(CADPartLandingView.NAME);
			
		
		});
		sideMenu.addMenuItem("Entry With Icon", FontAwesome.ANDROID, () -> {
			VerticalLayout content = new VerticalLayout();
			content.addComponent(new Label("Another layout"));
			sideMenu.setContent(content);
		});

		// User menu controls
		sideMenu.addMenuItem("Show/Hide user menu", FontAwesome.USER, () -> {
			sideMenu.setUserMenuVisible(!sideMenu.isUserMenuVisible());
		});
		
		
		router("");
	}
	
	private void router(String route){
		Notification.show(route);
		if(getSession().getAttribute("user") != null){
			getNavigator().addView(CADPartLandingView.NAME, CADPartLandingView.class);
			//getNavigator().addView(OtherSecurePage.NAME, OtherSecurePage.class);
		/*	if(route.equals("!OtherSecure")){
				getNavigator().navigateTo(OtherSecurePage.NAME);
			}else{
			*/	getNavigator().navigateTo(CADPartLandingView.NAME);
			//}
		}else{
			getNavigator().navigateTo(LoginView.NAME);
		}
	}
	

	private void setUser(String name, Resource icon) {
		sideMenu.setUserName(name);
		sideMenu.setUserIcon(icon);

		sideMenu.clearUserMenu();
		sideMenu.addUserMenuItem("Settings", FontAwesome.WRENCH, () -> {
			Notification.show("Showing settings", Type.TRAY_NOTIFICATION);
		});
		sideMenu.addUserMenuItem("Sign out", () -> {
			Notification.show("Logging out..", Type.TRAY_NOTIFICATION);
		});

		sideMenu.addUserMenuItem("Hide logo", () -> {
			if (!logoVisible) {
				sideMenu.setMenuCaption(menuCaption);
			} else {
				sideMenu.setMenuCaption(menuCaption);
			}
			logoVisible = !logoVisible;
		});
	}

}