package keln;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class KELN extends JPanel implements ActionListener, ItemListener{
	//variables
	JTable table;
	JScrollPane scroll_t, scroll_o;
	JTextArea output;
	JButton generate;
	JCheckBox[] researcher;
	JComboBox<String> selector;
	JPanel panel_North;
	JPanel panel_Checkbox;
	JPanel panel_Date;
	JTextField text_Month, text_Date;
	JLabel label_Month, label_Date;
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
			"Competent Cells/(μl)",
			"Medium"
	};
	String[] list_ColonyPCR_1 = {
			"NameⅠ" //環境依存文字
	};
	String[] list_ColonyPCR_2 = {
			"NameⅠ", //環境依存文字
			"NameⅡ"
	};
	String[] list_LiquidCulture = {
			"Name",
			"medium"
	};
	String[] list_Miniprep = {
			"DNA",
			"Concentration/(μg/ml)",
			"260/280", 
			"260/230"
	};
	String[] list_RestrictionEnzymeDigestion = {
			"Sample",
			"DNA/(μl)",
			"EcoR1/(μl)",
			"Xba1/(μl)",
			"Spe1/(μl)",
			"Pst1/(μl)",
			"Buffer/(μl)",
			"BSA/(μl)",
			"MilliQ/(μl)",
			"Total/(μl)"
	};
	String[] list_Electrophoresis = {
			"Lane",
			"Restriction Enzyme Digestion Products"
	};
	String[] list_GelExtraction = {
			"Lane",
			"Restriction Enzyme Digestion Product",
			"Volume/(μl)"
	};
	
	DefaultTableModel tablemodel;
	DefaultTableColumnModel colmodel;
	int Col_Max = 4;
	int Row_Max = 20;
	
	public KELN(){ //Constructor
		list_Current = list_PCR_Target; //実験の種類
		Col_Max = list_Current.length; //列の最大数を実験の種類の要素数で初期化
		createTable();
		output = new JTextArea();
		output.setRows(10);
		scroll_o = new JScrollPane(output);
		generate = new JButton("変換");
		generate.addActionListener(this);
		selector = new JComboBox<String>(list_Experiment);
		selector.addItemListener(this);
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
		//レイアウト関連の処理
		panel_Date = new JPanel(); //日付入力用パネル
		panel_North = new JPanel(); //チェックボックス、テーブルを含むパネル
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
		table.setColumnSelectionAllowed(true); //1セルごとに選択できるようにする
		table.setGridColor(Color.decode("#4682B4")); //罫線に色を設定
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
		
		//日付
		out += "<a name=\"";
		try{
			if(Integer.parseInt(text_Month.getText().toString()) <= 9){
				out += "0";
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "日付の値が不正です");
			return;
		}
		out += text_Month.getText().toString();
		try{
			if(Integer.parseInt(text_Date.getText().toString()) <= 9){
				out += "0";
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(this, "日付の値が不正です");
			return;
		}
		out += text_Date.getText().toString();
		out += "\" class = \"kyoto-jump\"></a>\n";
		out += "<h3>";
		out += text_Month.getText().toString() + "/" + text_Date.getText().toString();
		out += "</h3>\n";
		
		//実験名
		out += "<h4>";
		out += selector.getSelectedItem().toString();
		out += "</h4>\n";
		
		//実験者名
		out += "<span>";
		
		int finalindex = 0; //最後の実験者を表す番号
		for(int i=0; i<list_Researcher.length; i++){
			if(researcher[i].isSelected() == true){
				finalindex = i;
			}
		}
		for(int i=0; i<list_Researcher.length; i++){
			if(researcher[i].isSelected() == true){
				out += researcher[i].getText();
				if(i != finalindex){
					out += ", "; //最後の実験者にはカンマをつけない
				}
			}
		}
		out += "</span>\n";
		
		//表
		out += "<table>\n";
		out += "<tr>";
		
		//見出しをつける
		for(int i=0; i<Col_Max; i++){
			out += "<th>";
			col = colmodel.getColumn(i);
			out += col.getHeaderValue();
			out += "</th>";
		}
		out += "</tr>\n";
		
		//表をデータで埋める
		for(int i=0; i<Row_Max; i++){
			//1列丸ごと空白ならそこで止める
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
			//テーブルを更新するためには＞http://oshiete.goo.ne.jp/qa/4543611.html
		}
	}
}