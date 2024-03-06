package org.mbari.scommons.etc.xml

import org.w3c.dom.NodeList
import org.w3c.dom.Node

given Conversion[NodeList, List[Node]] with
    def apply(nodeList: NodeList): List[Node] =
        (0 until nodeList.getLength).map(nodeList.item).toList

