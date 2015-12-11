package keln;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class KELN extends JPanel implements ActionListener, ItemListener{
	//variables
	final String KELN_VERSION = "1.0";
	
	JTable table;
	JScrollPane scroll_t, scroll_o;
	JTextArea output;
	JButton generate;
	JCheckBox[] researcher;
	JComboBox<String> selector;
	JComboBox<String> author;
	JPanel panel_North;
	JPanel panel_Checkbox;
	JPanel panel_Date;
	JTextField text_Month, text_Date;
	JLabel label_Month, label_Date, label_Author;
	String[] list_Researcher = {
			"Sukegawa",
			"Matsumoto",
			"Li",
			"Tsujii",
			"Uchino",
			"Iguchi",
			"Michimori",
			"Notsu",
			"Kaneko",
			"Egashira",
			"Wan",
			"Yamamoto",
			"Yoshida",
			"Kim",
			"Nakamura",
			"Yamada"
	};
	String[] list_Experiment = {
			"PCR (Target)",
			"PCR (Steps)",
			"Transformation",
			"Colony PCR (1Column)",
			"Colony PCR (2Column)",
			"Liquid Culture",
			"Miniprep",
			"Restriction Enzyme Digestion",
			"Electrophoresis",
			"Gel Extraction"
	};
	String[] list_Current;
	String[] list_PCR_Target = {
			"Target",
			"Volume"
	};
	String[] list_PCR_Steps = {
			"Steps",
			"Temparature",
			"Time",
			"Cycle"
	};
	String[] list_Transformation = {
			"Sample Name",
			"Sample Volume",
			"Competent Cells/(��l)",
			"Medium"
	};
	String[] list_ColonyPCR_1 = {
			"Name�T" //���ˑ�����
	};
	String[] list_ColonyPCR_2 = {
			"Name�T", //���ˑ�����
			"Name�U"
	};
	String[] list_LiquidCulture = {
			"Name",
			"medium"
	};
	String[] list_Miniprep = {
			"DNA",
			"Concentration/(��g/ml)",
			"260/280", 
			"260/230"
	};
	String[] list_RestrictionEnzymeDigestion = {
			"Sample",
			"DNA/(��l)",
			"EcoR1/(��l)",
			"Xba1/(��l)",
			"Spe1/(��l)",
			"Pst1/(��l)",
			"Buffer/(��l)",
			"BSA/(��l)",
			"MilliQ/(��l)",
			"Total/(��l)"
	};
	String[] list_Electrophoresis = {
			"Lane",
			"Restriction Enzyme Digestion Products"
	};
	String[] list_GelExtraction = {
			"Lane",
			"Restriction Enzyme Digestion Product",
			"Volume/(��l)"
	};
	
	DefaultTableModel tablemodel;
	DefaultTableColumnModel colmodel;
	int Col_Max = 4;
	int Row_Max = 20;
	
	public KELN(){ //Constructor
		list_Current = list_PCR_Target; //�����̎��
		Col_Max = list_Current.length; //��̍ő吔�������̎�ނ̗v�f���ŏ�����
		createTable();
		output = new JTextArea();
		output.setRows(10);
		scroll_o = new JScrollPane(output);
		generate = new JButton("�ϊ�");
		generate.addActionListener(this);
		selector = new JComboBox<String>(list_Experiment);
		selector.addItemListener(this);
		author = new JComboBox<String>(list_Researcher);
		researcher = new JCheckBox[list_Researcher.length];
		for(int i = 0; i<list_Researcher.length; i++){
			researcher[i] = new JCheckBox(list_Researcher[i]);
		}
		text_Month = new JTextField();
		text_Date = new JTextField();
		text_Month.setPreferredSize(new Dimension(50, 25));
		text_Date.setPreferredSize(new Dimension(50, 25));
		label_Month = new JLabel("Month");
		label_Date = new JLabel("Day");
		label_Author = new JLabel("Author");
		//���C�A�E�g�֘A�̏���
		panel_Date = new JPanel(); //���t���͗p�p�l��
		panel_North = new JPanel(); //�`�F�b�N�{�b�N�X�A�e�[�u�����܂ރp�l��
		panel_Checkbox = new JPanel();
		panel_Date.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel_North.setLayout(new BorderLayout());
		panel_Checkbox.setPreferredSize(new Dimension(600, 70));
		panel_Checkbox.setLayout(new FlowLayout(FlowLayout.LEFT));
		for(int i = 0; i<list_Researcher.length; i++){
			panel_Checkbox.add(researcher[i]);
		}
		setLayout(new BorderLayout());
		panel_Date.add(label_Month);
		panel_Date.add(text_Month);
		panel_Date.add(label_Date);
		panel_Date.add(text_Date);
		panel_Date.add(label_Author);
		panel_Date.add(author);
		panel_North.add(panel_Date, BorderLayout.NORTH);
		panel_North.add(panel_Checkbox, BorderLayout.CENTER);
		panel_North.add(scroll_t, BorderLayout.SOUTH);
		add(panel_North, BorderLayout.NORTH);
		add(generate, BorderLayout.CENTER);
		add(scroll_o, BorderLayout.SOUTH);
		add(selector, BorderLayout.WEST);
	}
	
	public void createTable(){
		Col_Max = list_Current.length;
		tablemodel = new DefaultTableModel(list_Current, Row_Max);
		table = new JTable(tablemodel);
		table.setColumnSelectionAllowed(true); //1�Z�����ƂɑI���ł���悤�ɂ���
		table.setGridColor(Color.decode("#4682B4")); //�r���ɐF��ݒ�
		colmodel = (DefaultTableColumnModel)table.getColumnModel();
		scroll_t = new JScrollPane(table);
		TableColumn col;
		for(int i=0; i< Col_Max; i++){
			col = colmodel.getColumn(i);
			col.setHeaderValue(list_Current[i]);
		}
	}
	
	public void ConvertToHTML(){ //Convert JTable into HTML format
		TableColumn col;
		String out = "";
		
		//�o�[�W�������
		out += "<!-- Table Generated by KELN ver." + KELN_VERSION + " ";
		out += "Author: " + author.getSelectedItem().toString();
		out += " -->\n";
		
		//���t
		out += "<a name=\"";
		try{
			if(Integer.parseInt(text_Month.getText().toString()) <= 9){
				out += "0";
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "���t�̒l���s���ł�");
			return;
		}
		out += text_Month.getText().toString();
		try{
			if(Integer.parseInt(text_Date.getText().toString()) <= 9){
				out += "0";
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "���t�̒l���s���ł�");
			return;
		}
		out += text_Date.getText().toString();
		out += "\" class = \"kyoto-jump\"></a>\n";
		out += "<h3>";
		out += text_Month.getText().toString() + "/" + text_Date.getText().toString();
		out += "</h3>\n";
		
		//������
		out += "<h4>";
		out += selector.getSelectedItem().toString();
		out += "</h4>\n";
		
		//�����Җ�
		out += "<span>";
		
		int finalindex = 0; //�Ō�̎����҂�\���ԍ�
		for(int i=0; i<list_Researcher.length; i++){
			if(researcher[i].isSelected() == true){
				finalindex = i;
			}
		}
		for(int i=0; i<list_Researcher.length; i++){
			if(researcher[i].isSelected() == true){
				out += researcher[i].getText();
				if(i != finalindex){
					out += ", "; //�Ō�̎����҂ɂ̓J���}�����Ȃ�
				}
			}
		}
		out += "</span>\n";
		
		//�\
		out += "<table>\n";
		out += "<tr>";
		
		//���o��������
		for(int i=0; i<Col_Max; i++){
			out += "<th>";
			col = colmodel.getColumn(i);
			out += col.getHeaderValue();
			out += "</th>";
		}
		out += "</tr>\n";
		
		//�\���f�[�^�Ŗ��߂�
		for(int i=0; i<Row_Max; i++){
			//1��ۂ��Ƌ󔒂Ȃ炻���Ŏ~�߂�
			int emptyCount = 0;
			for(int j=0; j<Col_Max; j++){
				if(tablemodel.getValueAt(i, j) == null) emptyCount++ ;
			}
			if(emptyCount >= Col_Max) break;
			
			out += "<tr>";
			for(int j=0; j<Col_Max; j++){
				out += "<td>";
				try{
					out += tablemodel.getValueAt(i, j).toString();
				}catch(NullPointerException e){
					out += "";
				}
				out += "</td>";
			}
			out += "</tr>\n";
		}
		
		out += "</table>\n";
		out += "<!------------ Table END ------------>";
		output.setText(out);
	}
	
	public static void main(String[] args){
		KELN keln = new KELN();
		JFrame frame = new JFrame("KELN");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(keln);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == generate){
			ConvertToHTML();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == selector && e.getStateChange() == ItemEvent.SELECTED){
			switch (selector.getSelectedItem().toString()) {
			case "PCR (Target)":
				list_Current = list_PCR_Target;
				break;
			case "PCR (Steps)":
				list_Current = list_PCR_Steps;
				break;
			case "Transformation":
				list_Current = list_Transformation;
				break;
			case "Colony PCR (1Column)":
				list_Current = list_ColonyPCR_1;
				break;
			case "Colony PCR (2Column)":
				list_Current = list_ColonyPCR_2;
				break;
			case "Liquid Culture":
				list_Current = list_LiquidCulture;
				break;
			case "Miniprep":
				list_Current = list_Miniprep;
				break;
			case "Restriction Enzyme Digestion":
				list_Current = list_RestrictionEnzymeDigestion;
				break;
			case "Electrophoresis":
				list_Current = list_Electrophoresis;
				break;
			case "Gel Extraction":
				list_Current = list_GelExtraction;
				break;
			default:
				break;
			}
			panel_North.remove(scroll_t);
			//repaint();
			createTable();
			panel_North.add(scroll_t, BorderLayout.SOUTH);
			revalidate();
			//�e�[�u�����X�V���邽�߂ɂ́�http://oshiete.goo.ne.jp/qa/4543611.html
		}
	}
}