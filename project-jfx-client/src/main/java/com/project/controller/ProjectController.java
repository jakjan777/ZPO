package com.project.controller; 

//Standardowe biblioteki Javy (Core, Czas, Współbieżność)
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//2. Biblioteki JavaFX (Aplikacja, Kolekcje, Zdarzenia, Układ)
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Callback;
import javafx.util.StringConverter;

//3. Kontrolki JavaFX (UI)
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

//4. Logowanie (SLF4J)
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//5. Warstwy Twojego projektu (DAO, Model)
import com.project.dao.ProjektDAO;
import com.project.dao.ProjektDAOImpl;
import com.project.model.Projekt;


public class ProjectController { 
private static final Logger logger = LoggerFactory.getLogger(ProjectController.class); 
//Zmienne do obsługi stronicowania i wyszukiwania 
private String search4; 
private Integer pageNo; 
private Integer pageSize; 

private ObservableList<Projekt> projekty; 

private ExecutorService wykonawca; 
private ProjektDAO projektDAO; 

private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
private static final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//Automatycznie wstrzykiwane komponenty GUI 
@FXML 
private ChoiceBox<Integer> cbPageSizes; 
   @FXML 
   private TableView<Projekt> tblProjekt;  
   @FXML 
   private TableColumn<Projekt, Integer> colId; 
   @FXML 
   private TableColumn<Projekt, String> colNazwa; 
   @FXML 
   private TableColumn<Projekt, String> colOpis; 
   @FXML 
   private TableColumn<Projekt, LocalDateTime> colDataCzasUtworzenia; 
   @FXML 
   private TableColumn<Projekt, LocalDate> colDataOddania; 
   @FXML 
   private TextField txtSzukaj; 
   @FXML 
   private Button btnDalej; 
   @FXML 
   private Button btnWstecz; 
   @FXML 
   private Button btnPierwsza; 
   @FXML 
   private Button btnOstatnia; 
   @FXML
   private Button btnDodaj;
   @FXML
   private Button btnRemove;
   @FXML
   private Label lblStrona;
 
   public ProjectController(ProjektDAO projektDAO) { 
	      this.projektDAO = projektDAO; 
	      wykonawca = Executors.newFixedThreadPool(1);// W naszej aplikacji wystarczy jeden wątek do pobierania  
	       // danych. Przekazanie większej liczby takich zadań do puli  
	   }           // jednowątkowej powoduje ich kolejkowanie i sukcesywne 
	       // wykonywanie. 
 
   @FXML 
   public void initialize() { //Metoda automatycznie wywoływana przez JavaFX zaraz po wstrzyknięciu  
      search4 = "";   //wszystkich komponentów. Uwaga! Wszelkie modyfikacje komponentów        
      pageNo = 0;   //(np. cbPageSizes) trzeba realizować wewnątrz tej metody. Nigdy  
      pageSize = 10;   //nie używaj do tego celu konstruktora. 
 
      cbPageSizes.getItems().addAll(5, 10, 20, 50, 100); 
      cbPageSizes.setValue(pageSize); 
      colId.setCellValueFactory(new PropertyValueFactory<Projekt, Integer>("projektId")); 
      colNazwa.setCellValueFactory(new PropertyValueFactory<Projekt, String>("nazwa")); 
      colOpis.setCellValueFactory(new PropertyValueFactory<Projekt, String>("opis")); 
      colDataCzasUtworzenia.setCellValueFactory(new PropertyValueFactory<Projekt, LocalDateTime>                                                                                         ("dataCzasUtworzenia")); 
      colDataOddania.setCellValueFactory(new PropertyValueFactory<Projekt, LocalDate>("dataOddania")); 
       
    //Utworzenie nowej kolumny 
      TableColumn<Projekt, Void> colEdit = new TableColumn<>("Edycja"); 
      colEdit.setCellFactory(column -> new TableCell<Projekt, Void>() { 
      private final GridPane pane; 
      {  // Blok inicjalizujący w anonimowej klasie wewnętrznej 
      Button btnEdit = new Button("Edycja"); 
      Button btnRemove = new Button("Usuń"); 
      Button btnTask = new Button("Zadania"); 
      btnEdit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
      btnRemove.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
      btnTask.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
      btnEdit.setOnAction(event -> { 
         edytujProjekt(getCurrentProjekt()); 
      }); 
      btnRemove.setOnAction(event -> { 
          Projekt p = getCurrentProjekt();
          if (p != null) {
              usunProjekt(p); // Wywołujemy metodę kontrolera i przekazujemy jej projekt
          }
      });
      btnTask.setOnAction(event -> { 
         
      }); 
      pane = new GridPane(); 
      pane.setAlignment(Pos.CENTER); 
      pane.setHgap(10); 
      pane.setVgap(10); 
      pane.setPadding(new Insets(5, 5, 5, 5)); 
      pane.add(btnTask, 0, 0); 
      pane.add(btnEdit, 0, 1); 
      pane.add(btnRemove, 0, 2); 
      } 
      private Projekt getCurrentProjekt() { 
         int index = this.getTableRow().getIndex(); 
         return this.getTableView().getItems().get(index); 
      } 
      @Override 
      protected void updateItem(Void item, boolean empty) { 
         super.updateItem(item, empty); 
         setGraphic(empty ? null : pane); 
      } 
           }); 
       
	   //Dodanie kolumny do tabeli 
	       tblProjekt.getColumns().add(colEdit); 
	   
	       //Ustawienie względnej szerokości poszczególnych kolumn (liczą się proporcje) 
	   colId.setMaxWidth(5000); 
	   colNazwa.setMaxWidth(10000); 
	   colOpis.setMaxWidth(10000); 
	   colDataCzasUtworzenia.setMaxWidth(9000); 
	   colDataOddania.setMaxWidth(7000); 
	   colEdit.setMaxWidth(7000); 
	   colEdit.setMinWidth(140);

      
      
      projekty = FXCollections.observableArrayList(); 
       
      //Powiązanie tabeli z listą typu ObservableList przechowującą projekty 
      tblProjekt.setItems(projekty); 
      
      wykonawca.execute(() -> loadPage(search4, pageNo, pageSize));  
      
      colDataCzasUtworzenia.setCellFactory(column -> new TableCell<Projekt, LocalDateTime>() { 
          @Override 
          protected void updateItem(LocalDateTime item, boolean empty) { 
             super.updateItem(item, empty); 
             if (item == null || empty) { 
              setText(null); 
             } else { 
              setText(dateTimeFormater.format(item)); 
             } 
          } 
       }); 
      
      // Reagowanie na zmianę rozmiaru strony przez użytkownika
      cbPageSizes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
              // 1. Zapisujemy nowy rozmiar strony do zmiennej klasowej
              this.pageSize = newValue;
              
              // 2. Resetujemy licznik, aby zawsze zaczynać przeglądanie od pierwszej strony
              this.pageNo = 0;
              
              // 3. Pobieramy dane z bazy danych w tle
              wykonawca.execute(() -> loadPage(search4, pageNo, pageSize));
              
              // 4. Opcjonalnie: Jeśli masz Label do wyświetlania numeru strony (np. fx:id="lblStrona")
              // Platform.runLater(() -> lblStrona.setText("strona " + (pageNo + 1)));
          }
      });
   } 
 
   //Grupa metod do obsługi przycisków 
   @FXML
   private void onActionBtnSzukaj(ActionEvent event) {
       String fraza = txtSzukaj.getText() != null ? txtSzukaj.getText().trim() : "";
       
       this.search4 = fraza;
       this.pageNo = 0;
       wykonawca.execute(() -> loadPage(search4, pageNo, pageSize));
   }
 
   @FXML 
   private void onActionBtnDalej(ActionEvent event) {
	    // Uruchamiamy obliczenia i pobieranie w tle, aby nie zamrozić interfejsu
	    wykonawca.execute(() -> {
	        try {
	            int totalRows = 0;

	            // Sprawdzamy całkowitą liczbę rekordów w zależności od tego, czy filtrujemy dane
	            if (search4 != null && !search4.trim().isEmpty()) {
	                String cleanedSearch = search4.trim();
	                
	                if (cleanedSearch.matches("[0-9]+")) {
	                    totalRows = 1; // Dla szukania po ID maksymalnie będzie 1 wynik
	                } else if (cleanedSearch.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
	                    LocalDate dataOddania = LocalDate.parse(cleanedSearch, dateFormatter);
	                    totalRows = projektDAO.getRowsNumberWhereDataOddaniaIs(dataOddania);
	                } else {
	                    totalRows = projektDAO.getRowsNumberWhereNazwaLike(cleanedSearch);
	                }
	            } else {
	                totalRows = projektDAO.getRowsNumber();
	            }

	            // Obliczamy maksymalny indeks strony
	            int maxPageNo = (int) Math.ceil((double) totalRows / pageSize) - 1;
	            if (maxPageNo < 0) maxPageNo = 0; // Zabezpieczenie dla pustej bazy

	            // Jeśli obecna strona jest mniejsza niż maksymalna, zwiększamy licznik i ładujemy dane
	            if (pageNo < maxPageNo) {
	                pageNo++;
	                loadPage(search4, pageNo, pageSize);
	                
	            } else {
	                logger.info("Osiągnięto już ostatnią stronę wyników.");
	            }

	        } catch (RuntimeException e) {
	            logger.error("Błąd podczas przechodzenia do kolejnej strony", e);
	            Platform.runLater(() -> showError("Błąd paginacji", e.getMessage()));
	        }
	    });
	}
 
   @FXML 
   private void onActionBtnWstecz(ActionEvent event) {
	    // Sprawdzamy, czy nie jesteśmy już na pierwszej stronie (strona 0)
	    if (pageNo > 0) {
	        pageNo--; // Cofamy o jedną stronę
	        
	        // Ładujemy dane asynchronicznie w tle
	        wykonawca.execute(() -> loadPage(search4, pageNo, pageSize));
	    } else {
	        logger.info("Jesteś już na pierwszej stronie.");
	    }
	} 
 
   @FXML
   private void onActionBtnPierwsza(ActionEvent event) {
       // Sprawdzamy, czy w ogóle jest sens przełączać (czy nie jesteśmy już na 0)
       if (pageNo > 0) {
           pageNo = 0; // Resetujemy licznik do pierwszej strony
           
           wykonawca.execute(() -> loadPage(search4, pageNo, pageSize));
       }
   }
 
   @FXML
   private void onActionBtnOstatnia(ActionEvent event) {
       // Przycisk "Ostatnia" wymaga najpierw obliczenia, ile w ogóle jest stron w bazie
       wykonawca.execute(() -> {
           try {
               int totalRows = 0;

               // Sprawdzamy liczbę rekordów z uwzględnieniem aktywnych filtrów wyszukiwania
               if (search4 != null && !search4.trim().isEmpty()) {
                   String cleanedSearch = search4.trim();
                   
                   if (cleanedSearch.matches("[0-9]+")) {
                       totalRows = 1; 
                   } else if (cleanedSearch.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
                       LocalDate dataOddania = LocalDate.parse(cleanedSearch, dateFormatter);
                       totalRows = projektDAO.getRowsNumberWhereDataOddaniaIs(dataOddania);
                   } else {
                       totalRows = projektDAO.getRowsNumberWhereNazwaLike(cleanedSearch);
                   }
               } else {
                   totalRows = projektDAO.getRowsNumber();
               }

               // Obliczamy numer ostatniej strony (np. 25 wierszy / 10 na stronę = maxPageNo 2)
               int maxPageNo = (int) Math.ceil((double) totalRows / pageSize) - 1;
               if (maxPageNo < 0) maxPageNo = 0; // Zabezpieczenie na wypadek braku danych

               // Jeśli nie jesteśmy jeszcze na ostatniej stronie, przechodzimy na nią
               if (pageNo < maxPageNo) {
                   pageNo = maxPageNo;
                   loadPage(search4, pageNo, pageSize);
               } else {
                   logger.info("Jesteś już na ostatniej stronie.");
               }

           } catch (RuntimeException e) {
               logger.error("Błąd podczas skoku do ostatniej strony", e);
               Platform.runLater(() -> showError("Błąd paginacji", e.getMessage()));
           }
       });
   }
 
   @FXML 
   private void onActionBtnDodaj(ActionEvent event) { 
	   edytujProjekt(new Projekt()); 
   } 
   
   private void usunProjekt(Projekt projekt) {
	    if (projekt == null) {
	        return; // Zabezpieczenie na wypadek kliknięcia w pusty wiersz
	    }

	    // Tworzenie okna dialogowego z żądaniem potwierdzenia (Alert typu CONFIRMATION)
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Potwierdzenie usunięcia");
	    alert.setHeaderText("Czy na pewno chcesz usunąć projekt: \"" + projekt.getNazwa() + "\"?");

	    // Wyświetlenie okna i oczekiwanie na decyzję użytkownika
	    Optional<ButtonType> result = alert.showAndWait();
	    
	    // Jeśli użytkownik kliknął OK, przechodzimy do usuwania
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        // Wykonujemy operację w tle, aby nie zamrozić interfejsu
	        wykonawca.execute(() -> {
	            try {
	                // Usuwamy z bazy danych za pomocą Twojego DAO
	                projektDAO.deleteProjekt(projekt.getProjektId());
	                
	                // Aktualizujemy listę i odświeżamy tabelę w wątku JavaFX
	                Platform.runLater(() -> {
	                    projekty.remove(projekt);
	                    tblProjekt.refresh();
	                });
	                
	            } catch (RuntimeException e) {
	                String errMsg = "Błąd podczas usuwania projektu z bazy.";
	                logger.error(errMsg, e);
	                Platform.runLater(() -> showError(errMsg, e.getMessage()));
	            }
	        });
	    }
	}
   
   private void loadPage(String search4, Integer pageNo, Integer pageSize) { 
	      try { 
	            final List<Projekt> projektList = new ArrayList<>(); 
	            if (search4 != null && !search4.isEmpty()) { 
	            	String cleanedSearch = search4.trim();

	                if (cleanedSearch.matches("[0-9]+")) { 
	                    try {
	                        Integer id = Integer.valueOf(cleanedSearch);
	                        Projekt p = projektDAO.getProjekt(id); 
	                        if (p != null) {
	                            projektList.add(p);
	                        }
	                    } catch (NumberFormatException e) {
	                        logger.warn("Błąd parsowania ID: " + cleanedSearch);
	                      
	                        projektList.addAll(projektDAO.getProjekty(pageNo * pageSize, pageSize));
	                    }

	                } else if (cleanedSearch.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) { 
	                    try {
	                        LocalDate dataOddania = LocalDate.parse(cleanedSearch, dateFormatter);
	                        projektList.addAll(projektDAO.getProjektyWhereDataOddaniaIs(dataOddania, pageNo * pageSize, pageSize));
	                    } catch (DateTimeParseException e) {
	                        logger.warn("Niepoprawny kalendarzowo format daty: " + cleanedSearch);

	                        projektList.addAll(projektDAO.getProjekty(pageNo * pageSize, pageSize));
	                    }

	                } else { 
	                    projektList.addAll(projektDAO.getProjektyWhereNazwaLike(cleanedSearch, pageNo * pageSize, pageSize)); 
	                } 

	            } else { 
	                projektList.addAll(projektDAO.getProjekty(pageNo * pageSize, pageSize)); 
	            }
	            
	            Platform.runLater(() -> { 
	               projekty.clear(); 
	               projekty.addAll(projektList); 
	               lblStrona.setText("strona " + (pageNo + 1));
	            }); 
	            
	 
	 
	      } catch (RuntimeException e) { 
	            String errMsg = "Błąd podczas pobierania listy projektów."; 
	            logger.error(errMsg, e); 
	            String errDetails = e.getCause() != null ?  
	                   e.getMessage() + "\n" + e.getCause().getMessage() 
	                     : e.getMessage(); 
	            Platform.runLater(() -> showError(errMsg, errDetails)); 
	      } 
	   } 
	    
	   /** Metoda pomocnicza do prezentowania użytkownikowi informacji o błędach */ 
	   private void showError(String header, String content) { 
	      Alert alert = new Alert(AlertType.ERROR); 
	      alert.setTitle("Błąd"); 
	      alert.setHeaderText(header); 
	      alert.setContentText(content); 
	      alert.showAndWait(); 
	   } 
	 
	   public void shutdown() { 
	     // Wystarczyłoby tylko samo wywołanie metody wykonawca.shutdownNow(), ale można również, tak jak poniżej, 
	     // zaimplementować wersję z oczekiwaniem na zakończenie wszystkich zadań wykonywanych w puli wątków.  
	     if(wykonawca != null) { 
	         wykonawca.shutdown(); 
	         try { 
	            if(!wykonawca.awaitTermination(5, TimeUnit.SECONDS)) 
	               wykonawca.shutdownNow(); 
	         } catch (InterruptedException e) { 
	            wykonawca.shutdownNow(); 
	         } 
	      } 
	   } 
	   
	   private void edytujProjekt(Projekt projekt) { 
	          Dialog<Projekt> dialog = new Dialog<>(); 
	          dialog.setTitle("Edycja"); 
	          if (projekt.getProjektId() != null) { 
	               dialog.setHeaderText("Edycja danych projektu"); 
	          } else { 
	               dialog.setHeaderText("Dodawanie projektu"); 
	          } 
	          dialog.setResizable(true); 
	          Label lblId = getRightLabel("Id: "); 
	          Label lblNazwa = getRightLabel("Nazwa: "); 
	          Label lblOpis = getRightLabel("Opis: "); 
	          Label lblDataCzasUtworzenia = getRightLabel("Data utworzenia: "); 
	          Label lblDataOddania = getRightLabel("Data oddania: "); 
	          Label txtId = new Label(); 
	          if (projekt.getProjektId() != null) 
	               txtId.setText(projekt.getProjektId().toString()); 
	          TextField txtNazwa = new TextField(); 
	          if (projekt.getNazwa() != null) 
	               txtNazwa.setText(projekt.getNazwa()); 
	          TextArea txtOpis = new TextArea(); 
	          txtOpis.setPrefRowCount(6); 
	          txtOpis.setPrefColumnCount(40); 
	          txtOpis.setWrapText(true); 
	          if (projekt.getOpis() != null) 
	               txtOpis.setText(projekt.getOpis()); 
	          Label txtDataUtworzenia = new Label(); 
	          if (projekt.getDataCzasUtworzenia() != null) 
	               txtDataUtworzenia.setText(dateTimeFormater.format(projekt.getDataCzasUtworzenia())); 
	 
	          DatePicker dtDataOddania = new DatePicker(); 
	          dtDataOddania.setPromptText("RRRR-MM-DD"); 
	          dtDataOddania.setConverter(new StringConverter<LocalDate>() { 
	               @Override 
	               public String toString(LocalDate date) { 
	                    return date != null ? dateFormatter.format(date) : null; 
	               } 
	 
	               @Override 
	               public LocalDate fromString(String text) { 
	                    return text == null || text.trim().isEmpty() ? null : LocalDate.parse(text, dateFormatter); 
	               } 
	          }); 
	          dtDataOddania.getEditor().focusedProperty().addListener((obsValue, oldFocus, newFocus) -> { 
	               if (!newFocus) { 
	                    try { 
	                         dtDataOddania.setValue(dtDataOddania.getConverter().fromString( 
	       dtDataOddania.getEditor().getText())); 
	                    } catch (DateTimeParseException e) { 
	                         dtDataOddania.getEditor().setText(dtDataOddania.getConverter() 
	        .toString(dtDataOddania.getValue())); 
	                    } 
	               } 
	          }); 
	          
	          if (projekt.getDataOddania() != null) { 
	               dtDataOddania.setValue(projekt.getDataOddania()); 
	          } 
	 
	          GridPane grid = new GridPane(); 
	          grid.setHgap(10); 
	          grid.setVgap(10); 
	          grid.setPadding(new Insets(5, 5, 5, 5)); 
	          grid.add(lblId, 0, 0); 
	          grid.add(txtId, 1, 0); 
	          grid.add(lblDataCzasUtworzenia, 0, 1); 
	          grid.add(txtDataUtworzenia, 1, 1); 
	          grid.add(lblNazwa, 0, 2); 
	          grid.add(txtNazwa, 1, 2); 
	          grid.add(lblOpis, 0, 3); 
	          grid.add(txtOpis, 1, 3); 
	          grid.add(lblDataOddania, 0, 4); 
	          grid.add(dtDataOddania, 1, 4); 
	          dialog.getDialogPane().setContent(grid); 
	 
	          ButtonType buttonTypeOk = new ButtonType("Zapisz", ButtonData.OK_DONE); 
	          ButtonType buttonTypeCancel = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE); 
	          dialog.getDialogPane().getButtonTypes().add(buttonTypeOk); 
	          dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel); 
	          dialog.setResultConverter(new Callback<ButtonType, Projekt>() { 
	               @Override 
	               public Projekt call(ButtonType butonType) { 
	                    if (butonType == buttonTypeOk) { 
	                         projekt.setNazwa(txtNazwa.getText().trim()); 
	                         projekt.setOpis(txtOpis.getText().trim()); 
	                         projekt.setDataOddania(dtDataOddania.getValue()); 
	                         return projekt; 
	                    } 
	                    return null; 
	               } 
	          }); 
	 
	          Optional<Projekt> result = dialog.showAndWait(); 
	          if (result.isPresent()) { 
	               wykonawca.execute(() -> { 
	                    try { 
	                         projektDAO.setProjekt(projekt); 
	                         Platform.runLater(() -> { 
	                              if (tblProjekt.getItems().contains(projekt)) { 
	                                   tblProjekt.refresh(); 
	                              } else { 
	                                   tblProjekt.getItems().add(0, projekt); 
	                              } 
	                         }); 
	                    } catch (RuntimeException e) { 
	   String errMsg = "Błąd podczas zapisywania danych projektu!"; 
	   logger.error(errMsg, e); 
	   String errDetails = e.getCause() != null ?  
	  e.getMessage() + "\n" + e.getCause().getMessage() 
	: e.getMessage(); 
	   Platform.runLater(() -> showError(errMsg, errDetails)); 
	                    } 
	               }); 
	          } 
	   } 
	     private Label getRightLabel(String text) { 
	          Label lbl = new Label(text); 
	          lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
	          lbl.setAlignment(Pos.CENTER_RIGHT); 
	          return lbl; 
	     }
	     
	     private Projekt getCurrentProjekt() {
	    	    // Pobiera projekt, który jest aktualnie zaznaczony/podświetlony w tabeli przez użytkownika
	    	    return tblProjekt.getSelectionModel().getSelectedItem();
	    	}
	   
   
} 