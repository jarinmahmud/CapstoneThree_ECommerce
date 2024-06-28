package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }


    // create a new profile
    @Override
    public Profile create(Profile profile)
    {
        String query = "INSERT INTO profiles (user_id, first_name, last_name, " +
                "phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(
                Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            // get generated keys
            ResultSet generatedKeys = ps.getGeneratedKeys();
            while (generatedKeys.next()) {
                profile.setUserId(generatedKeys.getInt(1));
            }

            // return the new profile
            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // look for a profile by userId

    @Override
    public Profile getByUserId(int userId) {

        String query = "SELECT * FROM profiles WHERE user_id = ?";

        try (
                Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return mapRow(resultSet);

                // return nothing if profile does not exist
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // update a profile
    @Override
    public void update(Profile profile) {

        String query = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, " +
                "email = ?, address = ?, city = ?, state = ?, zip = ?" +
                "WHERE user_id = ?";

        try (
                Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getAddress());
            ps.setString(6, profile.getCity());
            ps.setString(7, profile.getState());
            ps.setString(8, profile.getZip());
            ps.setInt(9, profile.getUserId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Profile mapRow(ResultSet resultSet) throws SQLException {
        Profile profile = new Profile();
        profile.setUserId(resultSet.getInt("user_id"));
        profile.setFirstName(resultSet.getString("first_name"));
        profile.setLastName(resultSet.getString("last_name"));
        profile.setPhone(resultSet.getString("phone"));
        profile.setEmail(resultSet.getString("email"));
        profile.setAddress(resultSet.getString("address"));
        profile.setCity(resultSet.getString("city"));
        profile.setState(resultSet.getString("state"));
        profile.setZip(resultSet.getString("zip"));
        return profile;
    }
}