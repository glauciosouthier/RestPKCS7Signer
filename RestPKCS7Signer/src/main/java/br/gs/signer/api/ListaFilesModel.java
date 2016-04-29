package br.gs.signer.api;

import java.io.File;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ListaFilesModel extends AbstractTableModel {

	private Object[][] dados;

	private final String[] columnNames = { "Arquivo" };

	@Override
	public int getRowCount() {
		if (dados != null) {
			return dados.length;
		} else {
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dados[rowIndex][columnIndex];
	}

	public void populate(File[] files) {
		try {
			if (files != null) {
				int size=files.length;
				dados = new Object[size][columnNames.length];
				for (int i = 0; i < size; i++) {
					dados[i][0] = files[i].getName();
				}
				fireTableDataChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void populate(List<String> files) {
		try {
			if (files != null) {
				int size=files.size();
				dados = new Object[size][columnNames.length];
				for (int i = 0; i < size; i++) {
					dados[i][0] = files.get(i);
				}
				fireTableDataChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
