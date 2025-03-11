package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CompteBancaireTest {
    private CompteBancaire compte;
    private NotificationService notificationServiceMock;
    /* TP 1
      // Question1 :
    @Test
    public void testCreationCompteAvecSoldeInitial() {
        CompteBancaire compte = new CompteBancaire(100.0, notificationServiceMock);
        Assertions.assertEquals(100.0, compte.getSolde());
    }
    // Qeustion2 :
    @Test
    public void TestDepotArgent(){
       CompteBancaire compte = new CompteBancaire( 100.0 , notificationServiceMock);
       compte.deposer(50.0);
       Assertions.assertEquals(150.0 , compte.getSolde());
    }
        //Qeustion 3 :
    @Test
    public void TestRetraitArgent(){
        CompteBancaire compte = new CompteBancaire(100.0 ,notificationServiceMock);
        compte.retirer(30.0);
        Assertions.assertEquals(70.0 , compte.getSolde());
    }

    // Qeustion4 :
    @Test
    public  void TestRetraitMontantSup(){
        CompteBancaire compte = new CompteBancaire(100.0 ,notificationServiceMock);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> compte.retirer(200.0)
        );
        Assertions.assertEquals("Fonds insuffisants." ,exception.getMessage());
    }
    // Question 5  & 6 :
    @Test
    public  void TestRetraitNegative(){
        CompteBancaire compte = new CompteBancaire(100.0 , notificationServiceMock);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> compte.retirer(-10.0)
        );
        Assertions.assertEquals("Le montant du retrait doit être positif." ,exception.getMessage());
    }
    // Question 7 :
    @Test
    public void TestSoldNegativeInitial(){

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, ()->  new CompteBancaire(-50.0 , notificationServiceMock)
        );
        Assertions.assertEquals("Le solde initial ne peut pas être négatif." ,exception.getMessage());
    }
    */
    // TP 2 :
    //Tests unitaires avec JUnit 5, Mockito et JaCoCo
    @BeforeEach
    public void setUp() {
        // Question 1 : Cree un mock de NotificationService
        notificationServiceMock = Mockito.mock(NotificationService.class);
        compte = new CompteBancaire(100.0, notificationServiceMock);
    }
    // Test de creation d'un compte avec un solde initial correct
    @Test
    public void testCreationCompteAvecSoldeInitial() {
        Assertions.assertEquals(100.0, compte.getSolde());
    }

    // Question 2 : Verifier que la méthode envoyerNotification est appelee lors d'un depot
    @Test
    public void testDepotArgent() {
        double montant = 50.0;
        compte.deposer(montant);
        // Vérifie que le solde est bien mis à jour
        Assertions.assertEquals(150.0, compte.getSolde());
        // Vérifie que la notification est envoyée avec le bon message
        verify(notificationServiceMock).envoyerNotification("Dépôt de " + montant + " effectué.");
    }

    // Question 3 : Vérifier le comportement lorsqu'on tente de retirer plus que le solde
    @Test
    public void testRetraitMontantSup() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> compte.retirer(200.0)
        );
        Assertions.assertEquals("Fonds insuffisants.", exception.getMessage());
        // Vérifie qu'aucune notification n'est envoyée en cas d'erreur
        verify(notificationServiceMock, never()).envoyerNotification(anyString());
    }

    // Test pour un retrait avec montant négatif
    @Test
    public void testRetraitNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> compte.retirer(-10.0)
        );
        Assertions.assertEquals("Le montant du retrait doit être positif.", exception.getMessage());
    }

    // Test de création d'un compte avec un solde initial négatif
    @Test
    public void testSoldeNegativeInitial() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CompteBancaire(-50.0, notificationServiceMock)
        );
        Assertions.assertEquals("Le solde initial ne peut pas être négatif.", exception.getMessage());
    }
    // Question 4 : Vérifier qu'un mock est appelé un nombre précis de fois
    @Test
    public void testDeuxDepots() {
        compte.deposer(30.0);
        compte.deposer(20.0);
        // Vérifie que envoyerNotification a été appelée exactement 2 fois
        verify(notificationServiceMock, times(2)).envoyerNotification(anyString());
    }
    // Question 5 : Tester une interaction complexe avec transfert d'argent
    @Test
    public void testTransfererVers() {
        // Créer deux mocks pour les notifications des deux comptes
        NotificationService notificationSource = mock(NotificationService.class);
        NotificationService notificationDest = mock(NotificationService.class);

        // Créer deux comptes avec des soldes initiaux et leur mock respectif
        CompteBancaire source = new CompteBancaire(200.0, notificationSource);
        CompteBancaire destination = new CompteBancaire(50.0, notificationDest);

        double montant = 100.0;
        source.transfererVers(destination, montant);

        // Vérification des soldes
        Assertions.assertEquals(100.0, source.getSolde());
        Assertions.assertEquals(150.0, destination.getSolde());

        // Vérifier les notifications lors du retrait et du dépôt
        verify(notificationSource).envoyerNotification("Retrait de " + montant + " effectué.");
        verify(notificationDest).envoyerNotification("Dépôt de " + montant + " effectué.");

        // Vérifier les notifications spécifiques au transfert
        verify(notificationSource).envoyerNotification("Transfert de " + montant + " effectué depuis ce compte.");
        verify(notificationDest).envoyerNotification("Transfert de " + montant + " reçu sur ce compte.");
    }
}
