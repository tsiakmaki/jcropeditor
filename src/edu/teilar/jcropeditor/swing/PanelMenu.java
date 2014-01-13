/*
 * (C) Copyright 2010-2013 Maria Tsiakmaki.
 * 
 * This file is part of jcropeditor.
 *
 * jcropeditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) 
 * as published by the Free Software Foundation, version 3.
 * 
 * jcropeditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcropeditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.teilar.jcropeditor.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.event.DockFrontendListener;

/**
 * A menu that contains a {@link JCheckBoxMenuItem} for every
 * {@link Dockable} known to the {@link DockFrontend}. The user
 * can show and hide <code>Dockable</code>s by clicking on these
 * items.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PanelMenu extends JMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3661661094632629245L;
	
	/** the frontend which is used to show or hide <code>Dockable</code>s */
	private DockFrontend frontend;
	
	/**
	 * Creates a new menu that observes <code>frontend</code>. The items
	 * of the menu are read once from <code>frontend</code>, and will not
	 * change when new <code>Dockable</code>s are registered to <code>frontend</code>.
	 * @param frontend the list of <code>Dockable</code>s
	 */
	public PanelMenu( DockFrontend frontend ){
		this.frontend = frontend;
		setText( "Panels" );
		
		for( Dockable dockable : frontend.listDockables() )
			add( new Item( dockable ));
	}
	
	/**
	 * A menu item showing the visibility-state of one {@link Dockable}.
	 * @author Benjamin Sigg
	 */
	private class Item extends JCheckBoxMenuItem implements ActionListener, DockFrontendListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5785461410120506331L;
		
		/** whether the state currently is changing or not */
	    private boolean onChange = false;
	    /** the <code>Dockable</code> whose visibility-state is represented by this item */
		private Dockable dockable;

		/**
		 * Creates a new item.
		 * @param dockable the element whose visibility-state is represented by this item.
		 */
		public Item( Dockable dockable ){
			this.dockable = dockable;
			frontend.addFrontendListener( this );
			setIcon( dockable.getTitleIcon() );
			setText( dockable.getTitleText() );
			setSelected( frontend.isShown( dockable ));
			addActionListener( this );
		}
		
		public void actionPerformed( ActionEvent e ){
			if( !onChange ){
				onChange = true;
				try{
					if( isSelected() )
						frontend.show( dockable );
					else
						frontend.hide( dockable );
				}
				finally{
					onChange = false;
				}
			}
		}

		public void hidden( DockFrontend fronend, Dockable dockable ){
			if( dockable == this.dockable ){
				onChange = true;
				setSelected( false );
				onChange = false;
			}
		}

		public void shown( DockFrontend frontend, Dockable dockable ){
			if( dockable == this.dockable ){
				onChange = true;
				setSelected( true );
				onChange = false;
			}
		}

		public void added( DockFrontend frontend, Dockable dockable ) {
		    // ignore
		}
		
		public void removed( DockFrontend frontend, Dockable dockable ) {
		    // ignore
		}
		
		public void deleted( DockFrontend frontend, String name ){
			// ignore
		}

		public void loaded( DockFrontend frontend, String name ){
			// ignore
		}

		public void saved( DockFrontend frontend, String name ){
			// ignore
		}
		
		public void hideable( DockFrontend frontend, Dockable dockable, boolean hideable ) {
		    // ignore
		}
		
		public void read( DockFrontend frontend, String name ){
			// ignore
		}
	}
}
