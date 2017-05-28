class GetController < ApplicationController
	protect_from_forgery with: :null_session
	def list
		@users = User.all
		render json: @users.to_json
	end

	def check_login
		if User.exists?(username: params[:username])
			@user = User.find_by(username: params[:username])
			if @user.password == params[:password]
				render text: "success"
			else  
				render text: "incorrect_password"
			end
		else
			render text: "incorrect_username"
		end
	end

	def get_fcm_token
		@user = User.find_by(tracker_id: params[:tracker_id])
		if @user != nil then
			token = @user.fcm_token
			render text: token
		else
			render text: "null"
		end
	end

end
