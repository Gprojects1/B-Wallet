package repository

type TransactionRepository interface {
	AddNewUser()
	AddNewTrans()
	GetTranses()
	GetLastTrans()
	GetLastLocation()
	GetUserRiskLevel()
	GetUserLimits()
	GetPerTimeTransCount()
	IsUserBlocked()
	DeleteUser()
	DeleteTrans()
	UpdateUser()
	UpdateTrans()
	AddUserLimits()
	UpdateUserLimits()
}
