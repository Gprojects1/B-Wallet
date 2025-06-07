package model

type TransLimits struct {
	ID           uint `gorm:"primaryKey"`
	TransPerHour uint `gorm:"TransPerHour"`
	UserID       uint
	User         User `gorm:"foreignKey:UserID"`
}
