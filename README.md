# JavaOTP

Серверное приложение для генерации, отправки и валидации OTP кодов

### Эндпоинты

| Метод  | Путь                    | Описание                                  | Права       | Параметры                                                                                                                                   |
|--------|-------------------------|-------------------------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/auth/login`       | Вход в систему                            | -           | JSON: username, password                                                                                                                    |
| POST   | `/api/auth/register`    | Регистрация в системе                     | -           | JSON: username, password, is_admin, otp_destination (Sms, Email, Telegram, File), destination_address (Telegram chat id, email, tel number) |
|        |                         |                                           |             |                                                                                                                                             |
| GET    | `/api/admin/users`      | Список пользователей                      | ADMIN       | -                                                                                                                                           |
| DELETE | `/api/admin/users/{id}` | Удаление пользователя и привязанных кодов | ADMIN       | id пользователя в path                                                                                                                      |
| POST   | `/api/admin/otp_config` | Изменение конфига                         | ADMIN       | lifetime (секунды), length                                                                                                                  |
| GET    | `/api/admin/otp_config` | Получение конфига                         | ADMIN       | -                                                                                                                                           |
|        |                         |                                           |             |                                                                                                                                             |
| POST   | `/api/otp/generate`     | Генерация OTP кода                        | USER, ADMIN | operation_id                                                                                                                                |
| POST   | `/api/otp/validate`     | Проверка OTP кода                         | USER, ADMIN | operation_id, value                                                                                                                         |

**В каждый запрос (кроме /api/auth/*) необходимо прикладывать в хедер access_token, полученный при авторизации**

### Перед запуском

Необходимо в каждом .property файле задать нужные параметры (свои ключи и конфигурацию)

### Особенности

- Реализовано автоматическое удаление просроченных или использованных кодов