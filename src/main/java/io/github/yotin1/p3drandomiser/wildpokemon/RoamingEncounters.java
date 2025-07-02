package io.github.yotin1.p3drandomiser.wildpokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.github.yotin1.p3drandomiser.LegendaryPokemon;
import io.github.yotin1.p3drandomiser.P3DFile;
import io.github.yotin1.p3drandomiser.P3DMap;
import io.github.yotin1.p3drandomiser.Randomiser;

/**
 * An enumeration object contaning data of roaming Pokemon. Also
 * contains methods for randomising the script files of each encounter.
 *
 */
public enum RoamingEncounters {

	RAIKOU_ENTEI(null, null) {

		@Override
		public void randomise() {

			List<String> ids = new ArrayList<String>(
					Arrays.asList("243", "244"));

			List<String> newIds = new ArrayList<String>(
					Arrays.asList(Randomiser.getRandomLegendaryPokemon(), Randomiser.getRandomLegendaryPokemon()));

			List<String> newPokemonMusic = new ArrayList<String>();
			newIds.forEach(id -> {

				if (Randomiser.getCheckBoxes().get("hgssMusic")) {
					newPokemonMusic.add(LegendaryPokemon.getMap().get(id).getContentPackMusic());
				} else {
					newPokemonMusic.add(LegendaryPokemon.getMap().get(id).getNormalMusic());
				}
			});

			List<P3DFile> scriptFiles = new ArrayList<P3DFile>(Arrays.asList(
					new P3DFile(
							Randomiser.directory.resolve("Content\\Data\\Scripts\\ecruteak\\suicune_encounter.dat")),
					new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\eusine\\burnt1.dat"))));

			scriptFiles.forEach(file -> {

				List<String> data = file.getData();

				for (int index = 0; index < data.size(); index++) {

					String line = data.get(index);

					if (StringUtils.containsIgnoreCase(line, "@pokemon.newroaming(")) {

						String[] lineAsArray = StringUtils.split(file.getCommand(index), "(,)");

						int indexOfIdList = ids.indexOf(lineAsArray[0]);

						lineAsArray[0] = newIds.get(indexOfIdList);
						lineAsArray[4] = (newPokemonMusic.get(indexOfIdList) == null ? ""
								: newPokemonMusic.get(indexOfIdList));

						file.replaceCommand(index, StringUtils.join(lineAsArray, ","));
					}
				}

				file.writeFile();
			});

		}
	};

	protected final String id;
	protected String scriptPath;

	protected P3DFile scriptFile;
	protected String newId;

	RoamingEncounters(String scriptPath, String id) {

		this.id = id;

		this.scriptFile = (scriptPath != null
				? new P3DFile(Randomiser.directory.resolve("Content\\Data\\Scripts\\" + scriptPath))
				: null);

		this.newId = Randomiser.getRandomLegendaryPokemon();
	}

	public void randomise() {

	}
}
