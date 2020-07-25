package io.bankbridge.model;

public class BankModel {
	public String bic;
	public String name;
	public String countryCode;
	public String auth;

	@Override
	public String toString() {
		return "BankModel{" +
				"bic='" + bic + '\'' +
				", name='" + name + '\'' +
				", countryCode='" + countryCode + '\'' +
				", auth='" + auth + '\'' +
				'}';
	}
}
