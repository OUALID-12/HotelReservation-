package service;

import connexion.Connexion;
import dao.IDAO;
import entities.Reservation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ReservationService implements IDAO<Reservation> {

    @Override
    public boolean create(Reservation o) {
        String query = "INSERT INTO reservation ( datedebut, datefin, client_id, chambre_id) VALUES ( ?, ?, ?, ?)";
        try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
            ps.setDate(1, new Date(o.getDatedebut().getTime()));
            ps.setDate(2, new Date(o.getDatefin().getTime()));
            ps.setInt(3, o.getClient().getId());
            ps.setInt(4, o.getChambre().getId());

            int ligne = ps.executeUpdate();
            return ligne == 1;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    String q="select * from reservation where chamber_id = ''";
    
    
    @Override
    public boolean update(Reservation o) {
        String query = "UPDATE reservation SET datedebut = ?, datefin = ?, chambre_id = ?, client_id = ? WHERE id = ?";
        try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
            ps.setDate(1, new Date(o.getDatedebut().getTime()));
            ps.setDate(2, new Date(o.getDatefin().getTime()));
            ps.setInt(3, o.getChambre().getId());
            ps.setInt(4, o.getClient().getId());
            ps.setInt(5, o.getId());

            int ligne = ps.executeUpdate();
            return ligne == 1;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean delete(Reservation o) {
        String query = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
            ps.setInt(1, o.getId());

            int ligne = ps.executeUpdate();
            return ligne == 1;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public Reservation findById(int id) {
        String query = "SELECT * FROM reservation WHERE id = ?";
        try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Reservation(
                        rs.getInt("id"),
                        rs.getDate("datedebut"),
                        rs.getDate("datefin"),
                        new ClientService().findById(rs.getInt("client_id")),
                        new ChambreService().findById(rs.getInt("chambre_id"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
public boolean reserveroom(int chambreId, java.util.Date dateDebut, java.util.Date dateFin, int excludeReservationId) {
         // Convert java.util.Date to java.sql.Date
    java.sql.Date sqlDateDebut = new java.sql.Date(dateDebut.getTime());
    java.sql.Date sqlDateFin = new java.sql.Date(dateFin.getTime());
    String query = "SELECT COUNT(*) AS reservation_count FROM reservation " +
                   "WHERE chambre_id = ? " +
                   "AND ((datedebut <= ? AND datefin >= ?) OR " +
                   "(datedebut <= ? AND datefin >= ?) OR " +
                   "(datedebut >= ? AND datefin <= ?)) " +
                   "AND id != ?"; // Exclude the current reservation
    try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
        ps.setInt(1, chambreId);
        ps.setDate(2, sqlDateFin);
        ps.setDate(3, sqlDateDebut);
        ps.setDate(4, sqlDateDebut);
        ps.setDate(5, sqlDateFin);
        ps.setDate(6, sqlDateDebut);
        ps.setDate(7, sqlDateFin);
        ps.setInt(8, excludeReservationId); // Exclude the current reservation
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("reservation_count") > 0; // If count > 0, the room is already reserved
        }
    } catch (SQLException e) {
        System.out.println("Error checking room reservation: " + e.getMessage());
    }
    return false;
}
    @Override
    public List<Reservation> findAll() {
        String query = "SELECT * FROM reservation";
        List<Reservation> reservations = new ArrayList<>();
        try (PreparedStatement ps = Connexion.getCnx().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getDate("datedebut"),
                        rs.getDate("datefin"),
                        new ClientService().findById(rs.getInt("client_id")),
                        new ChambreService().findById(rs.getInt("chambre_id"))
                ));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return reservations;
    }
}