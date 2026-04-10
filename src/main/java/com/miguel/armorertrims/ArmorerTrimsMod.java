package com.miguel.armorertrims;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ArmorerTrimsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("armorertrims");

    // Lista donde guardaremos todas las plantillas de armadura existentes en el juego.
    private static final List<Item> ARMOR_TRIM_TEMPLATES = new ArrayList<>();

    @Override
    public void onInitialize() {
        LOGGER.info("¡Iniciando Armorer Trims Mod!");
        fillTrimList();
        registerCustomTrades();
    }

    private void fillTrimList() {
        // Estos son todos los IDs de las plantillas de armadura en Minecraft 1.21.11
        String[] trimIds = {
            "coast_armor_trim_smithing_template",
            "dune_armor_trim_smithing_template",
            "eye_armor_trim_smithing_template",
            "host_armor_trim_smithing_template",
            "raiser_armor_trim_smithing_template",
            "rib_armor_trim_smithing_template",
            "sentry_armor_trim_smithing_template",
            "shaper_armor_trim_smithing_template",
            "silence_armor_trim_smithing_template",
            "snout_armor_trim_smithing_template",
            "spire_armor_trim_smithing_template",
            "tide_armor_trim_smithing_template",
            "vex_armor_trim_smithing_template",
            "ward_armor_trim_smithing_template",
            "wayfinder_armor_trim_smithing_template",
            "wild_armor_trim_smithing_template",
            "flow_armor_trim_smithing_template",
            "bolt_armor_trim_smithing_template"
        };

        for (String id : trimIds) {
            // Buscamos el objeto Item en el registro del juego usando su ID.
            Item template = Registries.ITEM.get(Identifier.ofVanilla(id));
            if (template != Items.AIR) { // Si existe, lo añadimos a la lista.
                ARMOR_TRIM_TEMPLATES.add(template);
            } else {
                // Si no lo encuentra, mostramos un error en la consola.
                LOGGER.error("¡No se pudo encontrar la plantilla con ID: {}!", id);
            }
        }
        LOGGER.info("Se han cargado {} plantillas de armadura.", ARMOR_TRIM_TEMPLATES.size());
    }

    private void registerCustomTrades() {
        // Registramos el intercambio para el ALDEANO ARMERO (ARMORER) de nivel 5 (MAESTRO)
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 5, factories -> {
            factories.add((entity, random) -> {
                // 1. Seleccionar una plantilla aleatoria de nuestra lista.
                if (ARMOR_TRIM_TEMPLATES.isEmpty()) {
                    return null; // Si por algún motivo no hay plantillas, no creamos la oferta.
                }
                int randomIndex = random.nextInt(ARMOR_TRIM_TEMPLATES.size());
                ItemStack templateStack = new ItemStack(ARMOR_TRIM_TEMPLATES.get(randomIndex));

                // 2. Generar un precio aleatorio entre 15 y 45 esmeraldas.
                int randomPrice = 15 + random.nextInt(31); // 45 - 15 = 30, +1 para incluir el 45

                // 3. Crear la oferta de intercambio.
                // Parámetros: (Item de compra, Item de venta, Máx. de usos, XP para el aldeano, Multiplicador de precio)
                return new TradeOffer(
                        new ItemStack(Items.EMERALD, randomPrice), // Lo que el jugador paga
                        templateStack,                            // Lo que el jugador recibe
                        12,                                       // El intercambio se puede usar 12 veces
                        30,                                       // El aldeano gana 30 puntos de experiencia
                        0.05F                                     // Pequeña fluctuación en el precio
                );
            });
        });
        LOGGER.info("¡Intercambio personalizado registrado para el Armero nivel 5!");
    }
}
