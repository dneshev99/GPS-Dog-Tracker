class CreateUsers < ActiveRecord::Migration[5.0]
  def change
    create_table :users do |t|
      t.string :username
      t.string :password
      t.string :fcm_token
      t.string :tracker_id

      t.timestamps
    end
  end
end
