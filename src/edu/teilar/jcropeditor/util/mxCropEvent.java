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

package edu.teilar.jcropeditor.util;

/**
 * List crop editor project events 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class mxCropEvent {
	/**
	 * 
	 */
	public static final String SYNC_PREREQUISITES = "sync_prerequisites";
	
	public static final String KRC_NODE_REMOVED = "krc_node_delete";
	
	public static final String KRC_EDGE_REMOVED = "krc_edge_delete";
	
	
	public static final String XGRAPH_EDGE_DELETE = "xgraph_edge_delete";
	
	public static final String XGRAPH_NODE_DELETE = "xgraph_node_delete";
	
	
	public static final String KRC_LEARNING_OBJ_ADDED = "learning_object_added";
	public static final String KRC_LEARNING_OBJ_DELETED = "learning_object_deleted";
	
	
	public static final String XGRAPH_LEARNING_ACT_ADDED = "learning_act_added";
	
	public static final String KRC_LEARNING_OBJ_REMOVED = "learning_object_delete";
	
}
