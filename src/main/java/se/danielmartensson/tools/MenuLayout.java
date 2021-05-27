package se.danielmartensson.tools;

import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.stereotype.Component;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

import se.danielmartensson.configurations.security.SecurityConfiguration;
import se.danielmartensson.views.LxCurveView;
import se.danielmartensson.views.LxDataView;
import se.danielmartensson.views.RsqDataView;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "Vaadin Test Bench Data", shortName = "Test Bench Data")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Component
@UIScope
public class MenuLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public MenuLayout() {
    	init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
    			.withTitle("Vaadin Test Bench Data")
                .withAppMenu(LeftAppMenuBuilder.get()
                        .addToSection(HEADER,
                                new LeftHeaderItem("Test Bench Data", "Version 1.0", "images/BarPicture.png")
                        )
                        .add(LeftSubMenuBuilder.get("Bänk 515", VaadinIcon.PLUS.create())
                                        .add(new LeftNavigationItem("LX Data", VaadinIcon.DATABASE.create(), LxDataView.class),
                                             new LeftNavigationItem("LX Curve", VaadinIcon.LINE_CHART.create(), LxCurveView.class),
                                             new LeftNavigationItem("RSQ Data", VaadinIcon.DATABASE.create(), RsqDataView.class))
                                        .build())
                        .addToSection(FOOTER, 
                        		new LeftClickableItem("Logout", VaadinIcon.SIGN_OUT.create(), clickEvent -> UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT))
                        )
                        .build())
                .build());       
    }

}
