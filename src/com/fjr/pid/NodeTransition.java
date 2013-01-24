package com.fjr.pid;

import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NodeTransition {

	private double initY;
	private double newY;
	private double initMouseY;
	
	public NodeTransition(final Node node)
	{
		node.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				initY = node.getTranslateY();
				initMouseY = me.getSceneY();
			}
		});
		
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				double dragY = me.getSceneY() - initMouseY;
				newY = initY + dragY;
				node.setTranslateY(newY);
			}
		});
		
		node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// TODO Auto-generated method stub
				
				if (node.getTranslateY() > 20) {
					SequentialTransition trans = SequentialTransitionBuilder
							.create()
							.children(
									TranslateTransitionBuilder
											.create()
											.fromY(node
													.getTranslateY())
											.toY(10)
											.duration(
													Duration.seconds(1))
											.autoReverse(false)
											.cycleCount(1).build(),
									TranslateTransitionBuilder
											.create()
											.fromY(10)
											.toY(20)
											.autoReverse(false)
											.cycleCount(1)
											.duration(
													Duration.seconds(.3))
											.build())
							.node(node).build();
					trans.play();
				} else if (node.getTranslateY() < 20) {

					SequentialTransition trans = SequentialTransitionBuilder
							.create()
							.children(
									TranslateTransitionBuilder
											.create()
											.fromY(node
													.getTranslateY())
											.toY(30)
											.duration(
													Duration.seconds(1))
											.autoReverse(false)
											.cycleCount(1).build(),
									TranslateTransitionBuilder
											.create()
											.fromY(30)
											.toY(20)
											.autoReverse(false)
											.cycleCount(1)
											.duration(
													Duration.seconds(.2))
											.build())
							.node(node).build();
					trans.play();
				}
			}
		});
	}
}
