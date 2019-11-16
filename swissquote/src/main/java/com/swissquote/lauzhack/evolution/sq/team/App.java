package com.swissquote.lauzhack.evolution.sq.team;

import com.swissquote.lauzhack.evolution.SwissquoteEvolutionBuilder;
import com.swissquote.lauzhack.evolution.api.MarketProfile;
import com.swissquote.lauzhack.evolution.api.SwissquoteEvolution;

public class App {

	/**
	 * This is the starter for the application.
	 * You can keep this one, or create your own (using any Framework)
	 * As long as you run a SwissquoteEvolution
	 */
	public static void main(String[] args) {
		// Instantiate our BBook
		OurBBook ourBBook = new OurBBook();

		// Create the application runner
		SwissquoteEvolution app = new SwissquoteEvolutionBuilder().
				profile(MarketProfile.SOMETHING).
				seed(1).
				team("Centroid").
				bBook(ourBBook).
				filePath("/Users/Antoine/Documents/Projet/lauzhack2019/swissquote/output").
				interval(1).
				steps(5000).
				build();

		// Let's go !
		app.run();
		
		// Display the result as JSON in console (also available in the file at "Path")
		System.out.println(app.logBook());
	}

}