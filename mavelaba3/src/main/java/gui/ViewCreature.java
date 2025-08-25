package gui;

import objects.Creature;
import objects.Recipe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


public class ViewCreature extends JFrame {
    JLabel name;
    JTextField description;
    JFormattedTextField danger;
    JLabel areas;
    JTextField mention;
    JFormattedTextField height;
    JFormattedTextField weight;
    JTextField time;
    Creature creature;

    public ViewCreature(Creature creature) {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        this.creature = creature;
        name = new JLabel(creature.getName());
        description = new JTextField(creature.getDescription());
        danger = new JFormattedTextField(NumberFormat.getNumberInstance());
        danger.setValue(creature.getDangerLevel());
        areas = new JLabel(creature.getAreas().toString().substring(1, creature.getAreas().toString().length() - 1));
        mention = new JTextField(creature.getFirstMention());
        height = new JFormattedTextField(NumberFormat.getNumberInstance());
        weight = new JFormattedTextField(NumberFormat.getNumberInstance());
        height.setValue(creature.getHeight());
        weight.setValue(creature.getWeight());
        time = new JTextField(creature.getActivityTime());

        JLabel nameLabel = new JLabel("Name: ");
        JLabel descriptionLabel = new JLabel("Description: ");
        JLabel dangerLabel = new JLabel("Danger level: ");
        JLabel areaLabel = new JLabel("Area: ");

        JLabel mentionLabel = new JLabel("First mention: ");
        JLabel heightLabel = new JLabel("Height: ");
        JLabel weightLabel = new JLabel("Weight: ");
        JLabel timeLabel = new JLabel("Activity time: ");

        JPanel vulnPanel = new JPanel();
        vulnPanel.add(new JLabel("Vulnerabilities: "));
        for (String vuln : creature.getVulnerabilities().keySet()) {
            JLabel tempLabel = new JLabel(vuln);
            JLabel valueLabel = new JLabel(creature.getVulnerabilities().get(vuln) ? "уязвим" : "не уязвим");
            vulnPanel.add(tempLabel);
            vulnPanel.add(valueLabel);
        }

        Recipe recipe = creature.getRecipe();
        JPanel recipePanel = new JPanel();
        recipePanel.add(new JLabel("Recipe: "));
        recipePanel.add(new JLabel(recipe.getName()));
        recipePanel.add(new JLabel("Cooking time: "));
        recipePanel.add(new JLabel(String.valueOf(recipe.getCookingTime())));
        recipePanel.add(new JLabel("Effectiveness: "));
        recipePanel.add(new JLabel(recipe.getEffectiveness()));
        for (String ing : recipe.getIngredients().keySet()) {
            JLabel tempLabel = new JLabel(ing);
            JLabel valueLabel = new JLabel(String.valueOf(recipe.getIngredients().get(ing)));
            recipePanel.add(tempLabel);
            recipePanel.add(valueLabel);
        }

        JButton exit = new JButton("Return");
        exit.addActionListener(new ExitEvent());
        JButton save = new JButton("Save");
        save.addActionListener(new SaveEvent());

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nameLabel)
                                .addComponent(descriptionLabel)
                                .addComponent(dangerLabel)
                                .addComponent(areaLabel)
                                .addComponent(mentionLabel)
                                .addComponent(heightLabel)
                                .addComponent(weightLabel)
                                .addComponent(timeLabel)
                                .addComponent(vulnPanel)
                                .addComponent(recipePanel)
                                .addComponent(exit)
                                .addComponent(save))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name)
                                .addComponent(description)
                                .addComponent(danger)
                                .addComponent(areas)
                                .addComponent(mention)
                                .addComponent(height)
                                .addComponent(weight)
                                .addComponent(time)));


        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(nameLabel)
                                        .addComponent(name))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(descriptionLabel)
                                        .addComponent(description))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dangerLabel)
                                        .addComponent(danger))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(areaLabel)
                                        .addComponent(areas))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(mentionLabel)
                                        .addComponent(mention))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(heightLabel)
                                        .addComponent(height))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(weightLabel)
                                        .addComponent(weight))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(timeLabel)
                                        .addComponent(time))
                                .addComponent(vulnPanel)
                                .addComponent(recipePanel)
                                .addComponent(exit)
                                .addComponent(save)));

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class ExitEvent implements ActionListener {
        final ViewCreature gui = ViewCreature.this;

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.dispose();
        }
    }

    private class SaveEvent implements ActionListener {
        final ViewCreature gui = ViewCreature.this;

        @Override
        public void actionPerformed(ActionEvent e) {
            creature.setDescription(description.getText());
            creature.setDangerLevel(Integer.parseInt(danger.getText()));
            creature.setFirstMention(mention.getText());
            creature.setHeight(Double.parseDouble(height.getText()));
            creature.setWeight(Integer.parseInt(weight.getText()));
            creature.setActivityTime(time.getText());
            gui.dispose();
        }
    }
}