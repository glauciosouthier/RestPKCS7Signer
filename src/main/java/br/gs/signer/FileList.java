package br.gs.signer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class FileList {

	@XmlElement(required = true)
	protected List<String> files;

	public List<String> getFiles() {
		if (files == null) {
			files = new ArrayList<String>();
		}
		return this.files;
	}

}
