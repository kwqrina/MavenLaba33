package gui;

import objects.Creature;
import main.Controller;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GUI extends JFrame {
    private final DefaultMutableTreeNode rootNode;
    private final DefaultTreeModel treeModel;
    private Map<String, List<Creature>> creatures;
    private final Controller controller;

    public GUI(Controller controller) {
        super("Бестиарий");

        this.controller = controller;


        rootNode = new DefaultMutableTreeNode("Бестиарий");
        treeModel = new DefaultTreeModel(rootNode);
        JTree creatureTree = new JTree(treeModel);

        creatureTree.addTreeSelectionListener(new SelectionListener());
        TreeSelectionModel selModel = new DefaultTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        creatureTree.getSelectionModel().
                setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        add(new JScrollPane(creatureTree));

        JToolBar menuBar = new JToolBar();
        JButton importItem = new JButton("Импорт");
        JButton exportItem = new JButton("Экспорт");
        importItem.addActionListener(e -> importData());
        exportItem.addActionListener(e -> exportData());

        menuBar.add(importItem);
        menuBar.add(exportItem);
        add(menuBar, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();

            this.creatures = this.controller.importData(files);
            System.out.println(creatures);
            rootNode.removeAllChildren();

            for (String source : creatures.keySet()) {
                DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(source);
                for (Creature creature : creatures.get(source)) {
                    DefaultMutableTreeNode creatureNode = new DefaultMutableTreeNode(creature.getName());
                    sourceNode.add(creatureNode);
                }
                rootNode.add(sourceNode);
            }

            treeModel.reload();
        }
    }

    private void exportData() {
        boolean result = this.controller.exportData(creatures);
        JOptionPane.showMessageDialog(this, result ? "Успешно сохранено" : "Ошибка");
    }

    class SelectionListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            if (e.getPath().getPathCount() == 3) {
                var sourceCreatures = creatures.get(e.getPath().getPathComponent(1).toString());
                for (Creature creature : sourceCreatures) {
                    if (Objects.equals(creature.getName(), e.getPath().getPathComponent(2).toString())) {
                        new ViewCreature(creature);
                    }
                }
            }
        }

    }
}