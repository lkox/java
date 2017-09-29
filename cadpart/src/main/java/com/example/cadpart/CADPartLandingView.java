package com.example.cadpart;


import com.vaadin.annotations.Push;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import at.smartpart.beans.CadPart;
import at.smartpart.beans.MyCADPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.jouni.animator.Animator;
import org.vaadin.jouni.animator.client.CssAnimation;
import org.vaadin.jouni.dom.client.Css;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

@SpringView(name = CADPartLandingView.NAME)
//@StyleSheet("../../../VAADIN/themes/mytheme/login.css") // /cadpart/target/m2e-wtp/web-resources/META-INF  /cadpart/src/main/webapp/VAADIN/themes/mytheme/sliderpanel.css
@Theme("valo")
@Push//(PushMode.MANUAL)
public class CADPartLandingView extends VerticalLayout implements View {
	
	/**
	 * @auhthor Leopold Kogler 2017
	 */
	@Autowired
	private CADPartService service;

	Label currUser;
	public static final String NAME = "CADPartLandingView";
	private static final long serialVersionUID = 1L;
	private boolean responsive = true;
	TreeTable treeTable = new TreeTable();
	


	private MyCADPart cadPart;
	private PRT prt;
	private PRT myPrt;

	List<MyCADPart> cadParts = new ArrayList<MyCADPart>();

	private Grid cadPartGrid = new Grid();
	
	private TextField tFPrtNumber = new TextField("FileName");
	private TextField tFVersion = new TextField("version");


	SliderPanel secondTopSlider;
	SliderPanel firstTopSlider;
	VerticalLayout content;
	VerticalLayout memberPanel;
	private Button save = new Button("Save", e -> saveCADPart());

	private Tree tree;
	private List<PRT> prts = new ArrayList<PRT>();
	
	HorizontalSplitPanel split;

@PostConstruct
	void init(){
		
	HorizontalLayout hLayout = new HorizontalLayout();
	hLayout.setHeight(100, Unit.PERCENTAGE);
	hLayout.setWidth(100, Unit.PERCENTAGE);
	
	
	
	treeTable.addStyleName("tree-background");

		updateGrid();

		cadPartGrid.setColumns("filename", "version");
				
		cadPartGrid.setHeight(100, Unit.PERCENTAGE);
		cadPartGrid.setWidth(100, Unit.PERCENTAGE);
		cadPartGrid.setResponsive(responsive);
		cadPartGrid.addSelectionListener(e -> updateForm());
		
		String preparedSelect = "SELECT * from asms;";

		//prts = service.getPtcPrt(preparedSelect);
		//prt = prts.get(0);

		//memberPanel = createSidePanelContent(prt);

		//HorizontalLayout horLayout = new HorizontalLayout();

		//horLayout.setHeight(100, Unit.PERCENTAGE);
	//	horLayout.setWidth(200, Unit.PERCENTAGE); // bug?

	//	horLayout.setMargin(false);
	//	horLayout.setSpacing(false);

		/*firstTopSlider = new SliderPanelBuilder(createSearchBar(), "Search").expanded(false)
				.mode(SliderMode.TOP).caption("Search for Object")
				//.style(SliderPanelStyles.COLOR_BLUE)
				//.style(SliderPanelStyles.COLOR_WHITE)
				.tabPosition(SliderTabPosition.MIDDLE)
				.fixedContentSize(Page.getCurrent()
				.getBrowserWindowHeight()/2)
				.autoCollapseSlider(true)
				.build(); // System.out.println(rightSlider.getWidth()
		*/																						// +"
																								// "+

		secondTopSlider = new SliderPanelBuilder(content, "CADPart Details").tabPosition(SliderTabPosition.END)
				.mode(SliderMode.RIGHT)
				// .tabSize(Page.getCurrent().getBrowserWindowWidth()/2)
				//.style(SliderPanelStyles.COLOR_BLUE)
				.flowInContent(true)
				.fixedContentSize(Page.getCurrent().getBrowserWindowWidth()/2).tabPosition(SliderTabPosition.MIDDLE)
				.build();

	//	vLayout.addComponent(cadPartGrid);
	//	horLayout.addComponent(secondTopSlider);
	//	horLayout.setResponsive(responsive);

	
		
		split = new HorizontalSplitPanel();
	
		split.setFirstComponent(cadPartGrid);		
		split.setSecondComponent(memberPanel);
		split.setMaxSplitPosition(95, Unit.PERCENTAGE);
		hLayout.addComponent(split);
		
		VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
	
	//vSplitPanel.setSecondComponent(vLayout);
		//vSplitPanel.setSecondComponent(horLayout);
	//	vSplitPanel.setFirstComponent(createSearchBar());		
	//	vSplitPanel.setMaxSplitPosition(15, Unit.PERCENTAGE);
		
	//	VerticalLayout layout = new VerticalLayout(vSplitPanel);//firstTopSlider, horLayout);

		addComponent(hLayout);
		
		// , tFPrtNumber, tFmaxDim, tFmedDim, tFminDim,
		// tFVolume, tFSurface, tFDensity, tFTotalSurfaces, tFPlaneSurfaces,
		// save);

		//layout.
		setHeight(100, Unit.PERCENTAGE);
		//layout.
		setWidth(100, Unit.PERCENTAGE);
		//layout.
		setResponsive(responsive);
		

		//layout.
		setMargin(false);
		//layout.
		setSpacing(false);
		//setHeight(height);
		//setWidth(width);
		////////////////////////
		setResponsive(responsive);

	//	addComponent(layout);
		
		System.out.println("Found the page but empty !");
		
		
	}

	private HorizontalSplitPanel createSearchBar() {
		
		HorizontalSplitPanel vSplit = new HorizontalSplitPanel();
		HorizontalLayout pLayout = new HorizontalLayout();
		

		currUser = new Label();
		currUser.setSizeFull();
		currUser.setWidth(65, Unit.PERCENTAGE);

		List<String> list = new ArrayList<String>();
		list.add("prt_number");
		list.add("bbx_max");
		list.add("bbx_med");
		list.add("bbx_min");

		ComboBox comboBox = new ComboBox();
		comboBox.addItems(list);

		TextField tfSearch = new TextField();

//		Button btnSearch = new Button("search", e ->
//		new PreOrderBST());


		
		pLayout.addComponent(comboBox);
		pLayout.addComponent(tfSearch);
		pLayout.addComponent(new Button("search", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {

				String category = comboBox.getConvertedValue().toString();

				System.out.println("cat:" + category);

				String value = tfSearch.getValue();

				System.out.println("val:" + value);

				updateGridToSearchResult(category, value);

			}
		}));
/*
		searchPanel.setContent(pLayout);

		return searchPanel;
*/
		vSplit.setFirstComponent(currUser);
		vSplit.setSecondComponent(pLayout);
		
		return vSplit;
	}

	private VerticalLayout createSidePanelContent(PRT prt) {
		
				VerticalLayout component = new VerticalLayout();
				VerticalLayout rVertical = null;
		
		if(prt.getFileName().contains(".asm")){
			
			 createTreeContainer(cadPart.getPrtNumber());
		
		rVertical = new VerticalLayout();
		rVertical.addComponent(tree);
		
		} else if(prt.getFileName().contains(".drw")){
			
			System.out.println(" creating grid for "+ prt.getFileName() + " " + prt.getVersion());
			Grid drwMemberGrid = createDrwMemberContainer(prt);
			component.addComponent(drwMemberGrid);
		}
		
		
		
		
		
		String prt_number = prt.getFilename();
		String version = prt.getCurVersion();
		
		List<MyCADPart> cadParts = service.findByName(prt_number, version);
		cadPart = cadParts.get(cadParts.size());	
		Label lblPrtName1 = new Label(cadPart.getPrtNumber());
		component = new VerticalLayout(lblPrtName1);

		List<MySQLSidePanelBean> imageList = service.fetchPanelContent(cadPart.getId());

		System.out.println("ID " + cadPart.getId());

		StreamSource streamFromDb = new MySQLImageSource(imageList);

		StreamResource res = new StreamResource(streamFromDb, "Thumbnail");

		Image image = new Image("Thumbnail", res);
		
		double mywidth =.0;
		double myheight =.0;	
		double compWidth = component.getWidth(); 

		if(compWidth < 500)
		{
			mywidth = compWidth;
		}		
		else
		{
			mywidth = 500;
		}
			
		myheight = mywidth/3.0*2.0;
		
		int iWidth = (int) mywidth*2;
		int iHeight = (int) myheight*2;
		
		System.out.println("SCALING IMAGE TO : " + iWidth + " x " + iHeight);
	
		
		image.setWidth(iWidth, Unit.PIXELS);
		image.setHeight(iHeight, Unit.PIXELS);

		HorizontalSplitPanel vSplit = new HorizontalSplitPanel();
		
		VerticalLayout vertical = new VerticalLayout();
		
		vertical.addComponent(image);
		//vSplit.addComponent(image);
		
		vertical.setExpandRatio(image, 1);
		//vSplit.setExpandRatio(image, 1);
		
		
		FormLayout lpLayout = new FormLayout();
		FormLayout rpLayout = new FormLayout();
		
		Label lblPrtName = new Label("Dateiname");
		lpLayout.addComponent(lblPrtName);
		Label lblPrtNameCaption = new Label();
		lblPrtNameCaption.setCaption(cadPart.getPrtNumber());
		rpLayout.addComponent(lblPrtNameCaption);
		
		Label lblMaxDim = new Label("Abmessungen in X");
		lpLayout.addComponent(lblMaxDim);
		Label lblMaxDimCaption = new Label();
		lblMaxDimCaption.setCaption(cadPart.getMaxDim()+"");
		rpLayout.addComponent(lblMaxDimCaption);
		
		Label lblMedDim = new Label("Abmessungen in Y");
		lpLayout.addComponent(lblMedDim);
		Label lblMedDimCaption = new Label();
		lblMedDimCaption.setCaption(cadPart.getMedDim()+"");
		rpLayout.addComponent(lblMedDimCaption);
		
		Label lblMinDim = new Label("Abmessungen in Z");
		lpLayout.addComponent(lblMinDim);
		Label lblMinDimCaption = new Label();
		lblMinDimCaption.setCaption(cadPart.getMinDim()+"");
		rpLayout.addComponent(lblMinDimCaption);
		
		Label lblSurface = new Label("Oberfläche");
		lpLayout.addComponent(lblSurface);
		Label lblSurfaceCaption = new Label();
		lblSurfaceCaption.setCaption(customFormat(cadPart.getSurface()));
		rpLayout.addComponent(lblSurfaceCaption);
		
		Label lblVolume = new Label("Volumen");
		lpLayout.addComponent(lblVolume);
		Label lblVolumeCaption = new Label();
		lblVolumeCaption.setCaption(customFormat(cadPart.getVolume()));
		rpLayout.addComponent(lblVolumeCaption);
		
		vSplit.setFirstComponent(lpLayout);
		vSplit.setSecondComponent(rpLayout);
		vSplit.setMaxSplitPosition(50, Unit.PERCENTAGE);
		
		vertical.addComponent(vSplit);
		

		
		HorizontalSplitPanel hDivider50 = new HorizontalSplitPanel();
		hDivider50.setFirstComponent(vertical);
		hDivider50.setSecondComponent(rVertical);
		hDivider50.setMaxSplitPosition(50, Unit.PERCENTAGE);
		
		
		component.addComponent(hDivider50);

		component.addComponent(new Button("update", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {

				// createImagePanel(id);
				Notification.show("clicked: ", Notification.Type.HUMANIZED_MESSAGE);

			}
		}));
		component.setMargin(true);
		component.setSpacing(true);
		
		
		
		return component;

	}

	private Grid createDrwMemberContainer(PRT prt) {
		
		Grid drwMemberGrid = new Grid();

		
		
		drwMemberGrid.setColumns("filename", "version", "curdbversion");
				
		drwMemberGrid.setHeight(100, Unit.PERCENTAGE);
		drwMemberGrid.setWidth(100, Unit.PERCENTAGE);
		drwMemberGrid.setResponsive(responsive);
		//drwMemberGrid.addSelectionListener(e -> updateForm());
		
		updateSPGrid(prt, drwMemberGrid);
		
		return drwMemberGrid;
		
	}
	
	
	private void createTreeContainer(String prt_number){
		
		System.out.println("hello vaadin");
		
		//prt_number = "gehaeuse_ut.asm";
		//prt_number = "klappe_125_76n_cm.asm";
		//prt_number = "bgr0001.asm";		//clear existing data
		prt_number = "caddy.asm";
		treeTable.removeAllItems();
		
		List<PRT> cadAsms = service.getTreeForAsm(prt_number);

		PRT myAsm = cadAsms.get(0);
		
		//Map<String,JTree> container = myAsm.getTreeList();	
		
		JTree jtree = myAsm.getTree();	
		
		
		PreOrderBST preOrderBST2 = new PreOrderBST(jtree);
		preOrderBST2.createJTree(jtree);
		tree = preOrderBST2.returnTree();
		preOrderBST2.createBom();
		
		///////////////////////////////////////////////////////////////	
		//PreOrderBST preOrderBST = new PreOrderBST();		
		//preOrderBST.createVaadinTree(mdl);
		//tree = preOrderBST.returnTree();
	/*	
		for( Entry<String, JTree> entry:container.entrySet())
		{
			String name = entry.getKey();
			JTree mdl = entry.getValue();

			if(entry.getKey().equals(prt_number)){
				//PreOrderBST preOrderBST1 = new PreOrderBST(mdl);
				
				//preOrderBST1.createVaadinTree(mdl);
				//treeTable = preOrderBST1.returnTable();
				
				//////////////////////////////////////////////////////////////	
				PreOrderBST preOrderBST2 = new PreOrderBST(mdl);
				preOrderBST2.createJTree(mdl);
				tree = preOrderBST2.returnTree();
				preOrderBST2.createBom();
			}
			*/
		//}
	}


	
	public String customFormat(Object in) { 
		final String out = String.format("%12.2f", in);
		return out+"";
	}

	private void updateGrid() {
		
		String select = "Select * from fingerprint.drws WHERE filename like 'abzugsstoll%';";
		prts  = service.getPtcPrt(select);
		cadPartGrid.setContainerDataSource(new BeanItemContainer<>(PRT.class, prts));
		setFormVisible(false);
	}
	
	private void updateSPGrid(PRT prt, Grid drwMemberGrid) {
		
		//List<PRT> rootDrw = service.getDrwMemberList(prt_number, version);
		drwMemberGrid.removeAllColumns();
		drwMemberGrid.setContainerDataSource(new BeanItemContainer<>(CadPart.class, prt.getDrwMembers()));
		setFormVisible(false);
		
	}

	private void updateGridToSearchResult(String cat, String val) {
		cadParts = service.findByName(cat, val);
		cadPartGrid.setContainerDataSource(new BeanItemContainer<>(MyCADPart.class, cadParts));
		setFormVisible(false);
	}

	private void updateForm() {
		if (cadPartGrid.getSelectedRows().isEmpty()) {
			setFormVisible(false);
		} else {
			myPrt = (PRT) cadPartGrid.getSelectedRow();
			
			BeanFieldGroup.bindFieldsUnbuffered(myPrt, this);

			
			split.removeComponent(memberPanel);
			
			memberPanel = createSidePanelContent(myPrt);
			
			split.setSecondComponent(memberPanel);
			
		}
	}

	private void setFormVisible(boolean visible) {
		tFPrtNumber.setVisible(visible);
		tFVersion.setVisible(visible);
		save.setVisible(visible);
	}

	private void saveCADPart() {
		service.update(cadPart);
		updateGrid();
	}


	public VerticalLayout createImagePanel() {

		MyCADPart activePart = (MyCADPart) secondTopSlider.getData();

		VerticalLayout component;

		Label lblPrtName = new Label("id : " + activePart.getId());
		component = new VerticalLayout(lblPrtName);

		List<MySQLSidePanelBean> imageList = service.fetchPanelContent(activePart.getId());

		StreamSource streamFromDb = new MySQLImageSource(imageList);

		StreamResource res = new StreamResource(streamFromDb, "Thumbnail");

		Image image = new Image("Thumbnail", res);
		image.setWidth(400, Unit.PIXELS);
		image.setHeight(300, Unit.PIXELS);

		component.addComponent(image);

		component.setMargin(true);
		component.setSpacing(true);
		return component;
	}
	@Override
	public void enter(ViewChangeEvent event) {

	}

}
