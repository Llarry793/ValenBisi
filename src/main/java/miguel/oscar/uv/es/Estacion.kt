package miguel.oscar.uv.es

import android.os.Parcel
import android.os.Parcelable

data class EstacionBicis(
    val direccionEstacion: String,
    val numero: Int,
    val operativa: Boolean,
    val bicisDisponibles: Int,
    val postesLibres: Int,
    val totalPostes: Int, //Postes libres + bicis disponibles
    val ticket: Boolean,
    val ultimaActualizacion: String,
    val latitud: Double,
    val longitud: Double
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(direccionEstacion)
        parcel.writeInt(numero)
        parcel.writeByte(if (operativa) 1 else 0)
        parcel.writeInt(bicisDisponibles)
        parcel.writeInt(postesLibres)
        parcel.writeInt(totalPostes)
        parcel.writeByte(if (ticket) 1 else 0)
        parcel.writeString(ultimaActualizacion)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EstacionBicis> {
        override fun createFromParcel(parcel: Parcel): EstacionBicis {
            return EstacionBicis(parcel)
        }

        override fun newArray(size: Int): Array<EstacionBicis?> {
            return arrayOfNulls(size)
        }
    }
}

