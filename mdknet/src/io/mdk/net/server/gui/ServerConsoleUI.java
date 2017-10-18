package io.mdk.net.server.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.NeoLog;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;

public class ServerConsoleUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerConsoleUI frame = new ServerConsoleUI();
					frame.setVisible(true);
					Log.Logger.setPreffered(new NeoLog());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerConsoleUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblServersUp = new JLabel("Server's Up and Running!");
		lblServersUp.setFont(new Font("Menlo", Font.PLAIN, 18));
		contentPane.add(lblServersUp, BorderLayout.NORTH);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Menlo", Font.PLAIN, 18));
		contentPane.add(textArea, BorderLayout.CENTER);
		NeoLog.handlers.add(CommonInterfaces.getPrinter(textArea));
	}

}
