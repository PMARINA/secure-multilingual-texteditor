package gutil;

import gutil.PercentInfo;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author dkruger
 * This is my minimalist Layout manager using the old Motif concept
 * of placing objects using percentage of parent rectangle +/- delta pixels.
 * Adds the specified component to the layout, using PercentInfo to hold the constraints
 * Many of the methods are incomplete because I never needed to remove elements
 */
public class PercentLayout implements LayoutManager2 {
	private ArrayList<PercentInfo> percentInfo;
  
	public PercentLayout() {
		this.percentInfo = new ArrayList<>();
	}
  
	public void setConstraints(Component comp, Object layout) {	
	}
    
	@Override
	//The addLayoutComponent() method is called by the add(name, component) method of Container.
	public void addLayoutComponent(Component comp, Object constraints) {
		PercentInfo info = (PercentInfo) constraints;
		percentInfo.add(info);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(200, 200);
	}
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.1f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.1f;
	}

	@Override
	public void invalidateLayout(Container target) {
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return parent.getPreferredSize();
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return parent.getMinimumSize();
	}

	@Override
	//This method is called when target is first displayed and whenever it is resized
	public void layoutContainer(Container parent) {
		//Component[] ncomponents = parent.getComponents();
		int nComponent = parent.getComponentCount();
		//System.out.println("ha" + nComponent);
		int width = parent.getWidth();
		int height = parent.getHeight();
		for (int i = 0; i < nComponent; i++) {
			Component c = parent.getComponent(i);
			PercentInfo info = percentInfo.get(i);
			int x1 = (int) (info.px1 * width + info.dx1);
			int y1 = (int) (info.py1 * height + info.dy1);
			int x2 = (int) (info.px2 * width + info.dx2);
			int y2 = (int) (info.py2 * height + info.dy2);
			//System.out.println(info.getWidth() + " " + wMulriple + " " + hMulriple + " " + x + " " + y + " " + w + " " + h);
			c.setBounds(x1, y1, x2 - x1, y2 - y1);
		}
	}   
}
