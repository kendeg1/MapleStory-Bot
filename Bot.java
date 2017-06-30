public class Main extends Application{

	final int TITLE_SPACE = 20, TEXT_SPACE = 30;
	final double SPACE_HEIGHT = 1.1;
	final String RIGHT = "Right", LEFT = "Left", UP = "Up", DOWN = "Down", ALT = "Alt";
	Bot bot;
	boolean started = false;
	
	public static void main(String[] args){		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		GridPane grid = new GridPane();
		ToggleGroup group = new ToggleGroup();
		ArrayList<CheckBox> buffs = new ArrayList<>();
		ArrayList<TextField> timeUse = new ArrayList<>();
		ArrayList<TextField> keyUse = new ArrayList<>();
		ArrayList<ComboBox<String>> movements = new ArrayList<>();
		ArrayList<TextField> timeForMovements = new ArrayList<>();
		Button btnStart;
		
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        RIGHT, LEFT, UP, DOWN, ALT
			    );
		
		for(int i = 0; i < 4; i++){
			movements.add(new ComboBox<>(options));
			movements.get(i).getSelectionModel().selectFirst();
			timeForMovements.add(new TextField());
			timeForMovements.get(i).setMaxSize(40, timeForMovements.get(i).getMaxHeight());
			timeForMovements.get(i).setPromptText("dur");
		}
		
		for(int i = 1; i <= 9; i++){
			buffs.add(new CheckBox("Buff " + i));
			timeUse.add(new TextField());
			timeUse.get(i-1).setPromptText("duration");
			timeUse.get(i-1).setMaxSize(60, timeUse.get(i-1).getMaxHeight());
			keyUse.add(new TextField());
			keyUse.get(i-1).setPromptText("key");
			keyUse.get(i-1).setMaxSize(40, keyUse.get(i-1).getMaxHeight());
		}

		RadioButton radioAttackHold = new RadioButton("Attack Hold");
		RadioButton radioAttackSpam = new RadioButton("Attack Spam");
		TextField attHoldKey = new TextField();
		TextField attSpamKey = new TextField();
		
		attHoldKey.setPromptText("Key");
		attSpamKey.setPromptText("Key");
		attHoldKey.setMaxSize(40, attHoldKey.getMaxHeight());
		attSpamKey.setMaxSize(40, attSpamKey.getMaxHeight());
		
		radioAttackHold.setSelected(true);
		radioAttackHold.setToggleGroup(group);
		radioAttackSpam.setToggleGroup(group);
		
		grid.setHgap(9);
	    grid.setVgap(20);
	    
		Scene scene = new Scene(new ScrollPane(grid), 550, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Label lblAttack = new Label("Attacks");
		lblAttack.setFont(Font.font(lblAttack.getFont().getFamily(), FontWeight.BOLD, lblAttack.getFont().getSize()));
		Label lblBuff = new Label("Buffs");
		lblBuff.setFont(Font.font(lblBuff.getFont().getFamily(), FontWeight.BOLD, lblBuff.getFont().getSize()));
		Label lblMove = new Label("Movements");
		lblMove.setFont(Font.font(lblMove.getFont().getFamily(), FontWeight.BOLD, lblMove.getFont().getSize()));
		
		grid.add(lblAttack, 0, 0);
		grid.add(radioAttackHold, 1, 1);
		grid.add(radioAttackSpam, 1, 2);
		grid.add(attHoldKey, 2, 1);
		grid.add(attSpamKey, 2, 2);
	    grid.add(lblBuff, 0, 3);
	    
	    for(int i = 0; i < buffs.size(); i++){
	    	grid.add(buffs.get(i), 1, 4+i);
	    }
		
	    for(int i = 0; i < keyUse.size(); i++){
	    	grid.add(keyUse.get(i), 2, 4+i);
	    }
	    
	    for(int i = 0; i < timeUse.size(); i++){
	    	grid.add(timeUse.get(i), 3, 4+i);
	    }
	    
	    grid.add(lblMove, 5, 0);
	    for(int i = 0; i < movements.size(); i++){
	    	grid.add(movements.get(i), 6, i + 1);
	    }
	    
	    for(int i = 0; i < timeForMovements.size(); i++){
	    	grid.add(timeForMovements.get(i), 7, i + 1);
	    }
	    
	    Button addMove = new Button("+");
	    Button removeMove = new Button("-");
	    grid.add(addMove, 6, movements.size()+1);
	    grid.add(removeMove, 7, movements.size()+1);
	    
	    removeMove.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(movements.size() > 0){
					grid.getChildren().remove(movements.get(movements.size()-1));
					grid.getChildren().remove(timeForMovements.get(timeForMovements.size()-1));
					movements.remove(movements.size()-1);
					timeForMovements.remove(timeForMovements.size()-1);
					grid.getChildren().remove(addMove);
					grid.getChildren().remove(removeMove);
					grid.add(addMove, 6, movements.size()+1);
				    grid.add(removeMove, 7, movements.size()+1);
				}
				
			}
		});
	    
	    addMove.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				movements.add(new ComboBox<>(options));
				movements.get(movements.size()-1).getSelectionModel().selectFirst();
				timeForMovements.add(new TextField());
				timeForMovements.get(timeForMovements.size()-1).setMaxSize(40, timeForMovements.get(timeForMovements.size()-1).getMaxHeight());
				timeForMovements.get(timeForMovements.size()-1).setPromptText("dur");
				grid.add(movements.get(movements.size()-1), 6, movements.size());
				grid.add(timeForMovements.get(timeForMovements.size()-1), 7, timeForMovements.size());
				grid.getChildren().remove(addMove);
				grid.getChildren().remove(removeMove);
				grid.add(addMove, 6, movements.size()+1);
			    grid.add(removeMove, 7, movements.size()+1);
			}
		});
	    
	    btnStart = new Button("Start Bottin :)");
	    btnStart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(!started){
					bot = new Bot(getAttackKey(), getIsHold(), getBuffs(), getBuffsDuration(), getMovements(), getMovementDuration());
					bot.startBotting();
					btnStart.setText("Stop Bot");
					started = true;
				}
				else {
					started = false;
					btnStart.setText("Start Bottin :)");
					bot.stopBotting();
				}
			}

			private ArrayList<Integer> getMovementDuration() {
				ArrayList<Integer> moveDurs = new ArrayList<>();
				for(int i = 0; i < timeForMovements.size(); i++){
					try {
						if(timeForMovements.get(i).getText() != "")
							moveDurs.add(Integer.parseInt(timeForMovements.get(i).getText()));
						else
							moveDurs.add(5);
					}
					catch (Exception e){
						moveDurs.add(5);
					}
				}
				return moveDurs;
			}

			private ArrayList<Integer> getMovements() {
				ArrayList<Integer> moveKeys = new ArrayList<>();
				for(int i = 0; i < movements.size(); i++){
					switch(movements.get(i).getSelectionModel().getSelectedItem()){
					case UP:
						moveKeys.add(KeyEvent.VK_UP);
						break;
					case DOWN:
						moveKeys.add(KeyEvent.VK_DOWN);
						break;
					case RIGHT:
						moveKeys.add(KeyEvent.VK_RIGHT);
						break;
					case LEFT:
						moveKeys.add(KeyEvent.VK_LEFT);
						break;
					case ALT:
						moveKeys.add(KeyEvent.VK_ALT);
						break;	
					default:
						break;							
					}
				}
				return moveKeys;
			}

			private ArrayList<Character> getBuffs() {
				ArrayList<Character> buffKeys = new ArrayList<>();
				for(int i = 0; i < buffs.size(); i++){
					if(buffs.get(i).isSelected()){
						try {
							buffKeys.add((keyUse.get(i).getText().charAt(0)));
						}
						catch (Exception e){
							buffKeys.add('b');
							System.out.println("Error");
						}
					}
				}
				return buffKeys;
			}

			private ArrayList<Integer> getBuffsDuration() {
				ArrayList<Integer> buffDurs = new ArrayList<>();
				for(int i = 0; i < buffs.size(); i++){
					if(buffs.get(i).isSelected()){
						try {
							if(timeUse.get(i).getText() != "")
								buffDurs.add(Integer.parseInt(timeUse.get(i).getText()));
							else
								buffDurs.add(180);
						}
						catch (Exception e){
							buffDurs.add(180);
						}
						//buffDurs.add(Integer.parseInt(timeUse.get(i).getText()));
					}
				}
				return buffDurs;
			}

			private boolean getIsHold() {
				return radioAttackHold.isSelected();
			}

			private Character getAttackKey() {
				try {
					if(radioAttackHold.isSelected() && attHoldKey.getText().trim() != ""){
						return attHoldKey.getText().charAt(0);
					}
					else if(attSpamKey.getText().trim() == "")
						return 'z';
					return attSpamKey.getText().charAt(0);
				}
				catch (Exception e){
					return 'z';
				}
			}
		});
	    
	    grid.add(btnStart, 3, 0);
	    
	    File file = new File("data");
	    try {
	    if(file.exists()){
	    	Scanner s = new Scanner(file);
	    	if(Integer.parseInt(s.nextLine()) == 2){
	    		radioAttackHold.setSelected(false);
	    		radioAttackSpam.setSelected(true);
	    	}
	    	attHoldKey.setText(s.nextLine());
	    	attSpamKey.setText(s.nextLine());
	    	
	    	for(int i = 0; i < buffs.size(); i++){
	    		if(s.nextBoolean()){
	    			buffs.get(i).setSelected(true);
	    		}
	    		s.nextLine();
	    		keyUse.get(i).setText(s.nextLine());
	    		timeUse.get(i).setText(s.nextLine());
	    	}
	    	
	    	int numberOfMovements = Integer.parseInt(s.nextLine());
	    	if(numberOfMovements > movements.size()){
	    		for(int i = 3; i < numberOfMovements; i++){
	    			movements.add(new ComboBox<>(options));
	    			movements.get(i).getSelectionModel().selectFirst();
	    			timeForMovements.add(new TextField());
	    			timeForMovements.get(i).setMaxSize(40, timeForMovements.get(i).getMaxHeight());
	    			timeForMovements.get(i).setPromptText("dur");
	    		}
	    	}
	    	else {
	    		for(int i = numberOfMovements; i < 3; i++){
		    		if(movements.size() > 0){
						grid.getChildren().remove(movements.get(movements.size()-1));
						grid.getChildren().remove(timeForMovements.get(timeForMovements.size()-1));
						movements.remove(movements.size()-1);
						timeForMovements.remove(timeForMovements.size()-1);
						grid.getChildren().remove(addMove);
						grid.getChildren().remove(removeMove);
						grid.add(addMove, 6, movements.size()+1);
					    grid.add(removeMove, 7, movements.size()+1);
		    		}
				}
	    	}
	    	for(int i = 0; i < movements.size(); i++){
	    		movements.get(i).getSelectionModel().select(Integer.parseInt(s.nextLine()));
	    		timeForMovements.get(i).setText(s.nextLine());
	    	}
	    	s.close();
	    }
	    } catch (Exception e){
	    	
	    }
	    
	    primaryStage.setOnCloseRequest(event -> {
		    File f = new File("data");
		    if(!f.exists())
				try {
					f.createNewFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    try {
				PrintWriter writer = new PrintWriter(f);
				if(radioAttackHold.isSelected())
					writer.println(1);
				else
					writer.println(2);
				writer.println(attHoldKey.getText());
				writer.println(attSpamKey.getText());
				for(int i = 0; i < buffs.size(); i++){
					writer.println(buffs.get(i).isSelected());
					writer.println(keyUse.get(i).getText());
					writer.println(timeUse.get(i).getText());
				}
				writer.println(movements.size());
				for(int i = 0; i < movements.size(); i++){
					writer.println(movements.get(i).getSelectionModel().getSelectedIndex());
					writer.println(timeForMovements.get(i).getText());
				}
				writer.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
