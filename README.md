A proof of concept Editor for C.R.O.P. Learning Objects written in Java. 

============
Related work
============
@inproceedings{hartonas2008learning, title={Learning Objects and Learning Services in the Semantic Web}, author={Hartonas, Chrysafis and Gana, Eleni}, booktitle={Advanced Learning Technologies, 2008. ICALT'08. Eighth IEEE International Conference on}, pages={584--586}, year={2008}, organization={IEEE} }

@article{hartonas2008adaptivity, title={Adaptivity for knowledge content in the semantic web}, author={Hartonas, Chrysafis and Gana, Eleni}, journal={Proceedings of KGCM}, year={2008} }

@inproceedings{TsiakmakiH11,
  author    = {Τσιακμάκη, Μαρία and Γκανα, Ελένη and Χαρτώνας, Χρυσαφης},
  title     = {Προσαρμοστικότητα ως Αναδυόμενη Ιδιότητα Σύνθεσης Υπηρεσιών Μάθησης στο Σημασιολογικό Ιστό},
  booktitle = {2ο Πανελλήνιο Συνέδριο στην Ένταξη και χρήση των ΤΠΕ στην Εκπαιδευτική διαδικασία},
  year      = {2011},
  pages     = {889-898},
  ee        = {www.etpe.eu/new/custom/pdf/etpe1764.pdf}
}

@inproceedings{DBLP:conf/bci/TsiakmakiH13,
  author    = {Maria Tsiakmaki and Chrysafis Hartonas},
  title     = {Implementing the CROP Reference Architecture: The CROP Learning Object Editor},
  booktitle = {BCI (Local)},
  year      = {2013},
  pages     = {72},
  ee        = {http://ceur-ws.org/Vol-1036/p72-Tsiakmaki.pdf},
  crossref  = {DBLP:conf/bci/2013l},
  bibsource = {DBLP, http://dblp.uni-trier.de}
}


========================
Running the jcrop editor
========================
java -Xmx1024m -Dvlcj.check=no -jar jcropeditor.jar

========================
Requirements, references 
========================

* owlapi-bin - for creating, parsing and manipulating the crop ontologies files 
[http://owlapi.sourceforge.net/]

* docking-frames-core - the functionality of Docking Frames, the open source Java Swing 
docking framework [http://dock.javaforge.com/]
* docking-frames-common - implementation and default setups of the core of Docking 
Frames, the open source Java Swing docking framework [http://dock.javaforge.com/]

* HermiT - for reasoning on the CROP ontologies [http://hermit-reasoner.com/]

* jgraphx - for drawing the graphs of the crop learning objects [http://www.jgraph.com/]

* java junit - for implementing various tests [junit.org/‎]

* vlcj: VLCJ provides a Java binding, using JNA, to the VideoLAN native media player.
Vlcj also uses: jna.jar, platform.jar [https://code.google.com/p/vlcj/]

* PDFRenderer-0.9.0: for PDFViewer used at edu.teilar.jcropeditor.swing.viewer.PDFViewer 
[https://java.net/projects/pdf-renderer/]

* Apache POI: for PPTViewer, used at edu.teilar.jcropeditor.swing.viewer.PPTViewer
Apache poi also uses: poi-scratchpad-3.8-20120326.jar, poi-ooxml-3.8-20120326.jar, 
xbean.jar, dom4j-1.6.1.jar, poi-ooxml-schemas-3.8-20120326.jar

* log4j: for exporting log files for the editor 
[http://logging.apache.org/log4j/]

* commons-lang3-3.1 - [commons.apache.org]

* SHEF - WYSIWYG HTML editor framework and embeddable component for Swing. 
http://shef.sourceforge.net/
edu.teilar.jcropeditor.swing.editor.text.HTMLEditorPaneExt
SHEF also uses: jtidy-8.0.jar, novaworx-syntax-0.0.7.jar, sam.jar

* Various demos from The Java Tutorials 
http://docs.oracle.com/javase/tutorial/uiswing under the BSD license. 

* VLCJ project - licensed under the terms of the GNU General Public License.  
https://github.com/caprica/vlcj/blob/master/src/main/java/uk/co/caprica/vlcj/component/EmbeddedMediaPlayerComponent.java
edu.teilar.jcropeditor.swing.viewer.VideoViewer


=======
License
=======
(C) Copyright 2010-2013 Maria Tsiakmaki.

jcropeditor is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License (LGPL) 
as published by the Free Software Foundation, version 3.

jcropeditor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with jcropeditor.  If not, see <http://www.gnu.org/licenses/>.
 
