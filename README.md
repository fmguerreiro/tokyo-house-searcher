## Tokyo house searcher

This project is a Clojure application that parses the Suumo website for rental listings and sorts them based on a linear model. It then delivers the sorted listings via email.

## Setup

1. Clone this repository.

2. Make sure you have [Clojure](https://clojure.org/guides/install_clojure) installed on your system.

3. Set up your email configuration in `.env` by copying from `.env.example`, including email and password.

## Usage

To run the application, execute the following command:

``` shell
clojure -M -m app.scraper
```

The application will initiate the parsing process on the Suumo website, apply the linear model to sort the listings, and send the sorted results to the designated email address.

You can customize the parsing and sorting behavior by modifying the code in `src/`. 

## Contributing

Contributions to this project are welcome! If you want to add new features or improve existing ones, please follow these steps:

1. Fork this repository.

2. Create a new branch for your changes.

3. Make the necessary modifications.

4. Submit a pull request with a clear description of your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
